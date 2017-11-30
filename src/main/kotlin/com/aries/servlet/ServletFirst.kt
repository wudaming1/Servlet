package com.aries.servlet

import com.aries.servlet.bean.ResponseBean
import com.aries.servlet.utils.JsonUtil
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ServletFirst:HttpServlet(){


    override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
        super.doPost(req, resp)
    }

    override fun doGet(request: HttpServletRequest, resp: HttpServletResponse) {
        resp.contentType = "text/html;charset=UTF-8"
        resp.characterEncoding = "UTF-8"

//        resp.contentType = "application/json"
        val out = resp.writer
//        val respMessage = ResponseBean()
//        respMessage.data = "congratulations! request success!"
//        respMessage.message = "success"
//        out.write(JsonUtil.writeValueAsString(respMessage))
//        out.flush()


        val result = "<h1>" + "大飞熊!!" + "</h1>"

        out.write(result)

    }
}