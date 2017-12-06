package com.aries.servlet.user

import com.aries.servlet.bean.HttpResultCode
import com.aries.servlet.bean.ResponseBean
import com.aries.servlet.bean.UserBean
import com.aries.servlet.error.ArgumentException
import com.aries.servlet.orm.DBManager
import com.aries.servlet.utils.JsonUtil
import java.nio.charset.Charset
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 注册
 */
class RegisterServlet : HttpServlet() {


    override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
        var message = ""
        val result = ResponseBean()
        val out = resp.writer
        if (req.getParameter("name").isEmpty()) {
            result.resultCode = HttpResultCode.PARAM_ERR
            message = "参数错误，用户名为空！"
        }
        if (req.getParameter("password").isEmpty()) {
            result.resultCode = HttpResultCode.PARAM_ERR
            message+="\n参数错误，用户名为空！"
        }
        if (!message.isEmpty()){
            result.message = message
        }else {
            val name = String(req.getParameter("name").toByteArray(Charset.forName("ISO8859-1")), Charset.defaultCharset())
            val password = String(req.getParameter("password").toByteArray(Charset.forName("ISO8859-1")), Charset.defaultCharset())
            var conn: Connection? = null
            var stmt: Statement? = null

            try {

                conn = DBManager.connect()
                stmt = conn.createStatement()
                val queSql = "select * from app_user where user_name ="+"$name"
                val queryRS = stmt.executeQuery(queSql)
                message = if (queryRS.next()) {
                    result.resultCode = HttpResultCode.FAIL
                    "用户名已存在，请登录！"
                } else {
                    val sql = "INSERT INTO app_user(user_name,password) Values($name,$password)"
                    stmt.execute(sql)
                    result.resultCode = HttpResultCode.SUCCESS
                    "success"
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
            result.message = message

        }
        out.println(JsonUtil.writeValueAsString(result))
    }
}