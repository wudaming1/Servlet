package com.aries.servlet.user

import com.aries.servlet.ModifyServletRequestWrapper
import com.aries.servlet.base.BaseServlet
import com.aries.servlet.bean.HttpResultCode
import com.aries.servlet.bean.ResponseBean
import com.aries.servlet.bean.UserInfoBean
import com.aries.servlet.jwt.JWTHelper
import com.aries.servlet.orm.DBManager
import com.aries.servlet.utils.JsonUtil
import java.sql.Connection
import java.sql.SQLException
import java.sql.Statement
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 获取用户信息
 */
class UserInfoServlet : BaseServlet() {


    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        super.doGet(req, resp)
        val result = ResponseBean()
        var userId = -1
        if (req is ModifyServletRequestWrapper) {
            userId = req.userId
        }
        var conn: Connection? = null
        var stmt: Statement? = null

        try {
            conn = DBManager.connect()
            stmt = conn.createStatement()
            val queSql = "select * from app_user where user_id = '$userId'"
            val queryRS = stmt.executeQuery(queSql)
            if (queryRS.next()) {
                val userInfo = UserInfoBean(queryRS.getString("user_name"),
                        queryRS.getString("img_url"),
                        queryRS.getString("sex"),
                        queryRS.getLong("birthday"))
                result.resultCode = HttpResultCode.SUCCESS
                result.data = JsonUtil.writeValueAsString(userInfo)
            } else {
                result.resultCode = HttpResultCode.FAIL
                result.message = "不存在此用户"
            }
            queryRS.close()
            stmt.close()
            conn.close()

        } catch (se: SQLException) {
            //处理 JDBC 错误
            result.resultCode = HttpResultCode.DATABASE_ERR
            result.message = "数据库连接错误！"
            se.printStackTrace()
        } catch (e: Exception) {
            // 处理 Class.forName 错误
            result.resultCode = HttpResultCode.DATABASE_ERR
            result.message = "数据库连接错误！"
            e.printStackTrace()
        } finally {

            try {
                stmt?.close()
            } catch (se: SQLException) {
                se.printStackTrace()
            }
            try {
                conn?.close()
            } catch (se: SQLException) {
                se.printStackTrace()
            }
        }
        resp.writer.println(JsonUtil.writeValueAsString(result))
    }

    override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {

    }
}