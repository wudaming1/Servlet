package com.aries.servlet.user

import com.aries.servlet.bean.UserBean
import com.aries.servlet.error.ArgumentException
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

    // JDBC驱动器名称
    val JDBC_DRIVER = "com.mysql.jdbc.Driver"
    //数据库的url，格式jdbc:mysql://[host:port],[host:port].../[database][?参数名1][=参数值1][&参数名2][=参数值2]...
    val DB_URL = "jdbc:mysql://localhost/Aries"
    val USER = "root"
    val PASS = "123456"
    override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
        if (req.getParameter("name").isEmpty()) {
            throw ArgumentException("名字没填！")
        }
        if (req.getParameter("password").isEmpty()) {
            throw ArgumentException("密码没填！")
        }
        val name = String(req.getParameter("name").toByteArray(Charset.forName("ISO8859-1")), Charset.defaultCharset())
        val password = String(req.getParameter("password").toByteArray(Charset.forName("ISO8859-1")), Charset.defaultCharset())
        var conn: Connection? = null
        var stmt: Statement? = null

        try {
            Class.forName(JDBC_DRIVER)

            conn = DriverManager.getConnection(DB_URL, USER, PASS)
            stmt = conn.createStatement()
            val queSql = "select * from app_user where user_name = $name"
            val queryRS = stmt.executeQuery(queSql)
            if (queryRS.next()) {
                queryRS.close()
                //todo 用户名重复
            }else{
                queryRS.close()
                val sql = "INSERT INTO app_user Values($name,$password)"
                stmt.execute(sql)
            }

            stmt.close()
            conn.close()

        } catch (se: SQLException) {
            //处理 JDBC 错误
            se.printStackTrace()
        } catch (e: Exception) {
            // 处理 Class.forName 错误
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

        val session = req.session
        val userInfo = UserBean(name, password)
        session.setAttribute("user", userInfo)
        resp.sendRedirect("userInfo")
    }
}