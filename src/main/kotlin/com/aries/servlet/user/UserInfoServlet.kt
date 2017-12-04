package com.aries.servlet.user

import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class UserInfoServlet:HttpServlet(){
    /**
     *  从cookie中读取用户名和密码。
     */
    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        super.doGet(req, resp)
    }
}