package com.aries.servlet.user

import com.aries.servlet.ModifyServletRequestWrapper
import com.aries.servlet.bean.ResponseBean
import com.aries.servlet.utils.JsonUtil
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class UserInfoServlet : HttpServlet() {

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        var userId = -1
        if (req is ModifyServletRequestWrapper) {
            userId = req.userId
        }
        resp.writer.println(JsonUtil.writeValueAsString(ResponseBean(message = userId.toString())))
    }

    override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {

    }
}