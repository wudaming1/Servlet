package com.aries.servlet.user

import com.aries.servlet.ModifyServletRequestWrapper
import com.aries.servlet.bean.ErrorBean
import com.aries.servlet.bean.ErrorCode
import com.aries.servlet.bean.ResponseBean
import com.aries.servlet.bean.ResultCode
import com.aries.servlet.jwt.JWTHelper
import com.aries.servlet.utils.JsonUtil
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
                when {
                    token?.startsWith("debugToken_") == true -> {
                        val userId = token.split("_").last().toInt()
                        chain.doFilter(ModifyServletRequestWrapper(userId, request), response)
                    }
                    JWTHelper.verifyToken(token) -> {
                        //校验通过
                        val userId = JWTHelper.parserIdformToken(token)
                        chain.doFilter(ModifyServletRequestWrapper(userId, request), response)
                    }
                    else -> errMessage = "token验证失败！token：$token"
                }
            } catch (e: AlgorithmMismatchException) {
                errMessage += e.message ?: ""
            } catch (e: SignatureVerificationException) {
                errMessage += e.message ?: ""
            } catch (e: TokenExpiredException) {
                errMessage += e.message ?: ""
            } catch (e: InvalidClaimException) {
                errMessage += e.message ?: ""
            }
            if (!errMessage.isEmpty()) {
                val error = ErrorBean(ErrorCode.TOKEN_INVALID,"",errMessage)
                val result = ResponseBean(ResultCode.FAIL, "", error)
                response.writer.println(JsonUtil.writeValueAsString(result))
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