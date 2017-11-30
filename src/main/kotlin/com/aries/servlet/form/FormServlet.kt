package com.aries.servlet.form

import com.aries.servlet.error.ArgumentException
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import com.sun.corba.se.spi.presentation.rmi.StubAdapter.request
import java.nio.charset.Charset


class FormServlet : HttpServlet() {

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        if (req.getParameter("first_name").isEmpty()){
            throw ArgumentException("名字没填！")
        }
        if (req.getParameter("last_name").isEmpty()){
            throw ArgumentException("姓氏没填！")
        }
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
                + req.getParameter("first_name") + "\n" +
                "  <li><b>姓氏</b>："
                + req.getParameter("last_name") + "\n" +
                "</ul>\n" +
                "</body></html>")
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
                + String(req.getParameter("first_name").toByteArray(Charset.forName("ISO8859-1")), Charset.defaultCharset()) + "\n" +
                "  <li><b>姓氏</b>："
                + String(req.getParameter("last_name").toByteArray(Charset.forName("ISO8859-1")), Charset.defaultCharset()) + "\n" +
                "</ul>\n" +
                "</body></html>")
    }
}