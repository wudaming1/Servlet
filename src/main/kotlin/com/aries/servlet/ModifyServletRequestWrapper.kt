package com.aries.servlet

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper

class ModifyServletRequestWrapper(val userId: Int, request: HttpServletRequest) : HttpServletRequestWrapper(request) {

}