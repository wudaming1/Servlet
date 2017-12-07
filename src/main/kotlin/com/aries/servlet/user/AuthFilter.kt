package com.aries.servlet.user

import com.aries.servlet.jwt.JWTHelper
import com.auth0.jwt.exceptions.AlgorithmMismatchException
import com.auth0.jwt.exceptions.InvalidClaimException
import com.auth0.jwt.exceptions.SignatureVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import javax.servlet.*
import javax.servlet.http.HttpServletRequest

/**
 * 鉴权过滤器，验证token是否正确
 */
class AuthFilter : Filter {
    override fun destroy() {
        println("AuthFilter destroy")
    }

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        if (request is HttpServletRequest) {
            val token = request.getHeader("token")
            var errMessage = ""
            try {
                if (JWTHelper.verifyToken(token)) {
                    //校验通过
                    chain.doFilter(request, response)
                }
            } catch (e: AlgorithmMismatchException) {
                errMessage += e.message?:""
            } catch (e: SignatureVerificationException) {
                errMessage += e.message?:""
            } catch (e: TokenExpiredException) {
                errMessage += e.message?:""
            } catch (e: InvalidClaimException) {
                errMessage += e.message?:""
            }
            if (!errMessage.isEmpty()){
                //todo 不要继续传递chain，在这里产生响应结果。
            }
        } else {

            chain.doFilter(request, response)
        }

    }

    override fun init(filterConfig: FilterConfig?) {
        filterConfig?.apply {
            println(getInitParameter("name"))
        }
        println("AuthFilter init")
    }
}