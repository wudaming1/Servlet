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
 * 登录
 */
class LoginServlet : BaseServlet() {


    override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
        super.doPost(req, resp)
        val name = String(req.getParameter("name").toByteArray(Charset.forName("ISO8859-1")), Charset.defaultCharset())
        val password = String(req.getParameter("password").toByteArray(Charset.forName("ISO8859-1")), Charset.defaultCharset())
        val out = resp.writer
        val result = ResponseBean()
        val error = ErrorBean()
        if (name.isEmpty()) {
            result.status = ResultCode.FAIL
            error.message = "用户名为空！"
            error.code = ErrorCode.PARAM_ERR
        }

        if (password.isEmpty()) {
            result.status = ResultCode.FAIL
            error.message += "密码为空！"
            error.code = ErrorCode.PARAM_ERR
        }

        if (result.status != ResultCode.FAIL) {
            var conn: Connection? = null
            var stmt: Statement? = null

            try {
                conn = DBManager.connect()
                stmt = conn.createStatement()
                val queSql = "select * from app_user where user_name = '$name'"
                val queryRS = stmt.executeQuery(queSql)
                error.message = if (queryRS.next()) {
                    if (password == queryRS.getString("password")) {
                        result.status = ResultCode.SUCCESS
                        val userId = queryRS.getInt("user_id")
                        val token = JWTHelper.generateJWT(userId)
                        JWTHelper.putToken(resp, token)
                        result.data = token
                        "status"
                    } else {
                        result.status = ResultCode.FAIL
                        error.code = ErrorCode.UNKNOWN
                        "密码错误"
                    }
                } else {
                    result.status = ResultCode.FAIL
                    error.code = ErrorCode.UNKNOWN
                    "不存在此用户"
                }
                queryRS.close()
                stmt.close()
                conn.close()
            } catch (se: SQLException) {
                //处理 JDBC 错误
                result.status = ResultCode.FAIL
                error.code = ErrorCode.DATABASE_ERR
                error.message = "数据库连接错误！"
                se.printStackTrace()
            } catch (e: Exception) {
                // 处理 Class.forName 错误
                result.status = ResultCode.FAIL
                error.code = ErrorCode.DATABASE_ERR
                error.message = "数据库连接错误！"
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
        }

        if (result.status == ResultCode.FAIL){
            result.error = error
        }

        out.println(JsonUtil.writeValueAsString(result))

    }
}