package com.aries.servlet.user

import com.aries.servlet.bean.HttpResultCode
import com.aries.servlet.bean.ResponseBean
import com.aries.servlet.orm.DBManager
import com.aries.servlet.utils.JsonUtil
import java.nio.charset.Charset
import java.sql.Connection
import java.sql.SQLException
import java.sql.Statement
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


/**
 * 登录
 */
class LoginServlet : HttpServlet() {


    override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
        val name = String(req.getParameter("name").toByteArray(Charset.forName("ISO8859-1")), Charset.defaultCharset())
        val password = String(req.getParameter("password").toByteArray(Charset.forName("ISO8859-1")), Charset.defaultCharset())
        resp.characterEncoding = "UTF-8"
        val out = resp.writer
        var message = ""
        val result = ResponseBean()
        if (name.isEmpty()) {
            result.resultCode = HttpResultCode.PARAM_ERR
            message = "用户名为空！"
        }

        if (password.isEmpty()) {
            result.resultCode = HttpResultCode.PARAM_ERR
            message += "密码为空！"
        }

        if (message.isEmpty()) {
            var conn: Connection? = null
            var stmt: Statement? = null

            try {
                conn = DBManager.connect()
                stmt = conn.createStatement()
                val queSql = "select * from app_user where user_name = '$name'"
                val queryRS = stmt.executeQuery(queSql)
                message = if (queryRS.next()) {
                    if (password == queryRS.getString("password")) {
                        result.resultCode = HttpResultCode.SUCCESS
                        "success"
                    } else {
                        result.resultCode = HttpResultCode.FAIL
                        "密码错误"
                    }
                } else {
                    result.resultCode = HttpResultCode.FAIL
                    "不存在此用户"
                }
                queryRS.close()
                stmt.close()
                conn.close()
            } catch (se: SQLException) {
                //处理 JDBC 错误
                result.resultCode = HttpResultCode.DATABASE_ERR
                message = "数据库连接错误！"
                se.printStackTrace()
            } catch (e: Exception) {
                // 处理 Class.forName 错误
                result.resultCode = HttpResultCode.DATABASE_ERR
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
        }
        result.message = message
        out.println(JsonUtil.writeValueAsString(result))

    }
}