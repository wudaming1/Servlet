package com.aries.servlet.form

import javax.servlet.*

class LoginFilter:Filter{
    override fun destroy() {
        println("login filter destroy")
    }

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        println("come in url:"+request.servletContext.contextPath)
        chain.doFilter(request,response)
    }

    override fun init(filterConfig: FilterConfig?) {
        filterConfig?.apply {
            println(getInitParameter("name"))
        }
        println("login filter init")
    }
}