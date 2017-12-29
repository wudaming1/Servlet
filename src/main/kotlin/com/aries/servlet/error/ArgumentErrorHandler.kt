package com.aries.servlet.error

import java.io.PrintWriter
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ArgumentErrorHandler:HttpServlet(){
    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        val throwable = req.getAttribute("javax.servlet.error.exception") as Throwable
        val statusCode = req.getAttribute("javax.servlet.error.status_code") as  Int
        var servletName = req.getAttribute("javax.servlet.error.servlet_name") as String
        var requestUri = req.getAttribute("javax.servlet.error.request_uri") as String

        if (servletName.isEmpty()){
            servletName = "unknown"
        }

        if (requestUri.isEmpty()){
            requestUri = "unknown"
        }

        resp.contentType = "text/html";

        val out = resp.getWriter();
        val title = "Error/Exception Information";
        val docType =
        "<!doctype html public \"-//w3c//dtd html 4.0 " +       "transitional//en\">\n"
        out.println(docType +
                "<html>\n" +
                "<head><title>" + title + "</title></head>\n" +
                "<body bgcolor=\"#f0f0f0\">\n");

        if (throwable == null && statusCode == null){
            out.println("<h2>Error information is missing</h2>");
            out.println("Please return to the <a href=\"" +             resp.encodeURL("http://localhost:8080/") +             "\">Home Page</a>.");
        }else if (statusCode != null){
            out.println("The status code : " + statusCode);
        }else{
            out.println("<h2>Error information</h2>");
            out.println("Servlet Name : " + servletName +
                    "</br></br>");
            out.println("Exception Type : " +
                    throwable::class.java.simpleName +
                    "</br></br>");
            out.println("The request URI: " + requestUri +
                    "<br><br>");
            out.println("The exception error: " +
                    throwable.message)
        }
        out.println("</body>")
        out.println("</html>")
    }

    override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
        doGet(req,resp)
    }
}