package com.aries.servlet.user

import com.aries.servlet.bean.UserBean
import com.aries.servlet.error.ArgumentException
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.nio.charset.Charset


class FormServlet : HttpServlet() {

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        if (req.getParameter("name").isEmpty()){
            throw ArgumentException("名字没填！")
        }
        if (req.getParameter("password").isEmpty()){
            throw ArgumentException("密码没填！")
        }

        val session = req.session
        val userInfo = UserBean(req.getParameter("name"),req.getParameter("password"))
        session.setAttribute("user",userInfo)
        resp.sendRedirect("userInfo")


    }

    override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
        resp.contentType = "text/html;charset=UTF-8"
        resp.characterEncoding = "UTF-8"
        val out = resp.writer
        val title = "使用GET方法获取表单数据"
        val docType = "<!doctype html public \"-//w3c//dtd html 4.0 \" + \"transitional//en\">\n"
        out.println(docType +
                "<html>\n" +
                "<head><title>" + title + "</title></head>\n" +
                "<body bgcolor=\"#f0f0f0\">\n" +
                "<h1 align=\"center\">" + title + "</h1>\n" +
                "<ul>\n" +
                "  <li><b>名字</b>："
                + String(req.getParameter("name").toByteArray(Charset.forName("ISO8859-1")), Charset.defaultCharset()) + "\n" +
                "  <li><b>姓氏</b>："
                + String(req.getParameter("password").toByteArray(Charset.forName("ISO8859-1")), Charset.defaultCharset()) + "\n" +
                "</ul>\n" +
                "</body></html>")
    }
}