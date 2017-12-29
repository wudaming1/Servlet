package com.aries.servlet.base

import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

open class BaseServlet:HttpServlet(){
    override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
        resp.characterEncoding = "UTF-8"
        
    }

    override fun doDelete(req: HttpServletRequest, resp: HttpServletResponse) {
        resp.characterEncoding = "UTF-8"
    }


    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        resp.characterEncoding = "UTF-8"
    }

    override fun doPut(req: HttpServletRequest, resp: HttpServletResponse) {
        resp.characterEncoding = "UTF-8"
    }

}