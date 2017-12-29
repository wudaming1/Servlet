package com.aries.servlet.user

import com.aries.servlet.ModifyServletRequestWrapper
import com.aries.servlet.base.BaseServlet
import com.aries.servlet.bean.*
import com.aries.servlet.orm.DBManager
import com.aries.servlet.utils.JsonUtil
import java.nio.charset.Charset
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import jdk.nashorn.internal.runtime.ScriptingFunctions.readLine
import java.io.InputStreamReader
import java.io.BufferedReader



/**
 * 获取用户信息
 * /auth/userInfo
 */
class UserInfoServlet : BaseServlet() {


    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        super.doGet(req, resp)
        val result = ResponseBean()
        val error = ErrorBean()
        var userId = -1
        if (req is ModifyServletRequestWrapper) {
            userId = req.userId
        }

        var conn:Connection? = null
        var pst:PreparedStatement? =null

        try {
            conn = DBManager.connect()
            val queSql = "select * from app_user where user_id =?"
            pst = conn.prepareStatement(queSql)
            pst.setInt(1,userId)
            val resultSet = pst.executeQuery()
            if(resultSet.next()){
                val userInfo = UserInfoBean(resultSet.getString("user_name"),
                        resultSet.getString("img_url"),
                        resultSet.getString("sex"),
                        resultSet.getLong("birthday"))
                result.status = ResultCode.SUCCESS
                result.data = JsonUtil.writeValueAsString(userInfo)
            }else{
                result.status = ResultCode.FAIL
                error.message = "用户不存在！"
            }
            resultSet?.close()

        }catch (se: SQLException) {
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
                conn?.close()
            } catch (se: SQLException) {
                se.printStackTrace()
            }

            try {
                pst?.close()
            }catch (se: SQLException) {
                se.printStackTrace()
            }
        }
        if (result.status == ResultCode.FAIL){
            result.error = error
        }

        resp.writer.println(JsonUtil.writeValueAsString(result))
    }

    override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {

    }

    /**
     *  Rest类无法取得HTTP PUT请求发送的参数
     *  rest类通过String id = request.getParameter("id");可以取得url传递过来的id参数值
     *  但是，Put中的参数无法取得
     *  可能是API只能解析GET、POST方式的表单提交。其他一些传统的MVC框架也有类似现象
     *
     *  参考[http://dbscx.iteye.com/blog/828167]
     */
    override fun doPut(req: HttpServletRequest, resp: HttpServletResponse) {
        super.doPut(req, resp)
        val input = BufferedReader(InputStreamReader(req.inputStream))
        var line: String? = ""
        var userInfo:UserInfoBean? = null
        while (line != null){
            line = input.readLine()
            if(line != null){
                val param = line.split("=").last()
                userInfo = JsonUtil.readValue<UserInfoBean>(param, UserInfoBean::class.java)
            }
            println(line)
        }

        val result = ResponseBean()
        val error = ErrorBean()
        var userId = -1
        if (req is ModifyServletRequestWrapper) {
            userId = req.userId
        }
        var conn: Connection? = null
        var pst:PreparedStatement? = null
        try {
            conn = DBManager.connect()
            val sql = "UPDATE app_user SET user_name=?, img_url=?, sex=?, birthday=? WHERE user_id =?"
            pst = conn.prepareStatement(sql)
            pst.setString(1, userInfo?.userName)
            pst.setString(2, userInfo?.imgUrl)
            pst.setString(3, userInfo?.sex)
            pst.setLong(4, userInfo?.birthday ?: 0L)
            pst.setInt(5, userId)
            if (pst.executeUpdate() == 0) {
                result.status = ResultCode.FAIL
                error.message = "不存在此用户"
            } else {
                result.status = ResultCode.SUCCESS
                result.data = JsonUtil.writeValueAsString(userInfo!!)

            }

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
                conn?.close()
            } catch (se: SQLException) {
                se.printStackTrace()
            }
            try {
              pst?.close()
            }catch (se: SQLException) {
                se.printStackTrace()
            }
        }

        if (result.status == ResultCode.FAIL){
            result.error = error
        }
        resp.writer.println(JsonUtil.writeValueAsString(result))
    }
}