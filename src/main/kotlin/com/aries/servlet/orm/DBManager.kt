package com.aries.servlet.orm

import java.sql.Connection
import java.sql.DriverManager

object DBManager {
    // JDBC驱动器名称
    private val JDBC_DRIVER = "com.mysql.jdbc.Driver"
    //数据库的url，格式jdbc:mysql://[host:port],[host:port].../[database][?参数名1][=参数值1][&参数名2][=参数值2]...
    private val DB_URL = "jdbc:mysql://localhost/Aries"
    private val USER = "root"
    private val PASS = "123456"

    fun connect(): Connection {
        Class.forName(JDBC_DRIVER)
        return DriverManager.getConnection(DB_URL, USER, PASS)
    }
}

object TableName{
    val USER = "app_user"
}