package com.aries.servlet.user

import com.aries.servlet.base.BaseServlet
import com.aries.servlet.bean.ErrorBean
import com.aries.servlet.bean.ErrorCode
import com.aries.servlet.bean.ResponseBean
import com.aries.servlet.bean.ResultCode
import com.aries.servlet.jwt.JWTHelper
import com.aries.servlet.orm.DBManager
import com.aries.servlet.utils.JsonUtil
import java.nio.charset.Charset
import java.sql.Connection
import java.sql.SQLException
import java.sql.Statement
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 注册
 */
class RegisterServlet : BaseServlet() {


    override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
        super.doPost(req, resp)
        var message = ""
        val result = ResponseBean()
        val error = ErrorBean()
        val out = resp.writer
        if (req.getParameter("name").isNullOrEmpty()) {
            result.status = ResultCode.FAIL
            error.code = ErrorCode.PARAM_ERR
            message = "参数错误，用户名为空！"
        }
        if (req.getParameter("password").isNullOrEmpty()) {
            result.status = ResultCode.FAIL
            error.code = ErrorCode.PARAM_ERR
            message += "\n参数错误，用户名为空！"
        }
        val name = String(req.getParameter("name").toByteArray(Charset.forName("ISO8859-1")), Charset.defaultCharset())
        val password = String(req.getParameter("password").toByteArray(Charset.forName("ISO8859-1")), Charset.defaultCharset())
        if (!message.isEmpty()) {
            error.message = message
        } else {

            var conn: Connection? = null
            var stmt: Statement? = null

            try {
                conn = DBManager.connect()
                stmt = conn.createStatement()
                val queSql = "select * from app_user where user_name = '$name'"
                val queryRS = stmt.executeQuery(queSql)
                message = if (queryRS.next()) {
                    result.status = ResultCode.FAIL
                    "用户名已存在，请登录！"
                } else {
                    val insertSql = "INSERT INTO app_user(user_name,password) Values('$name','$password')"
                    stmt.execute(insertSql)
                    val queryR = stmt.executeQuery(queSql)
                    while (queryR.next()) {
                        val userId = queryR.getInt("user_id")
                        val token = JWTHelper.generateJWT(userId)
                        JWTHelper.putToken(resp, token)
                        result.data = token
                    }
                    queryR.close()
                    result.status = ResultCode.SUCCESS
                    "status"
                }
                queryRS.close()
                stmt.close()
                conn.close()

            } catch (se: SQLException) {
                //处理 JDBC 错误
                result.status = ErrorCode.DATABASE_ERR
                message = "数据库连接错误！"
                se.printStackTrace()
            } catch (e: Exception) {
                // 处理 Class.forName 错误
                result.status = ErrorCode.DATABASE_ERR
                message = "数据库连接错误！"
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
            if (result.status == ResultCode.FAIL)
                result.error = error

        }
        out.println(JsonUtil.writeValueAsString(result))
    }

}