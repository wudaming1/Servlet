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
        val out = resp.writer
//        resp.contentType = "application/json"
//        val respMessage = ResponseBean()
//        respMessage.data = "congratulations! request success!"
//        respMessage.message = "success"
//        out.write(JsonUtil.writeValueAsString(respMessage))
//        out.flush()

        resp.contentType = "text/html;charset=UTF-8"
        val title = "使用GET获取表单"
        val doctype = "<!DOCTYPE html> \n"
        out.println(doctype +
                "<html>\n" +
                "<head><title>" + title + "</title></head>\n" +
                "<body bgcolor=\"#f0f0f0\">\n" +
                "<h1 align=\"center\">" + title + "</h1>\n" +
                "<ul>\n" +
                "  <li><b>名字</b>："
                + "first_name" + "\n" +
                "  <li><b>姓氏</b>："
                + ("last_name") + "\n" +
                "</ul>\n" +
                "</body></html>")

    }
}