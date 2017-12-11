package com.aries.servlet.jwt

import com.aries.servlet.bean.UserBean
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.AlgorithmMismatchException
import com.auth0.jwt.exceptions.InvalidClaimException
import com.auth0.jwt.exceptions.SignatureVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.sun.org.apache.xpath.internal.operations.Bool
import java.util.*
import javax.servlet.http.HttpServletResponse

object JWTHelper {

    private val AUDIENCE = "Ming"
    private val JWT_ID = "jti_jti"
    private val ISSUER = "aries"
    //有效期3天
    private val leeway = 3 * 24 * 60 * 60 * 1000L

    private val USER_ID = "user_id"

    fun generateJWT(user: UserBean): String {
        val algorithm = Algorithm.RSA256(MyRSAKeyProvider())
        val jwt = JWT.create()
                //在head部分添加自定义字段，不要添加敏感信息
                .withHeader(mapOf("aries" to "白羊座"))
                //在payload部分添加自定义字段，不要添加敏感信息
                .withClaim(USER_ID, user.id)
                //iat:指定jwt的签发时间
                .withIssuedAt(Date())
                //iss:jwt签发者
                .withIssuer(ISSUER)
                //exp:指定jwt的过期时间3天
                .withExpiresAt(Date(System.currentTimeMillis() + leeway))
                //aud:jwt接收方
                .withAudience(AUDIENCE)
                //sub:jwt所面向的用户
                .withSubject("all")
                //定义在什么时间之前，该jwt都是不可用的
                .withNotBefore(Date())
                //jwt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击
                .withJWTId(JWT_ID)
                //提供签名id
                .withKeyId("aries")
                //新建一个jwt，并用指定的算法进行签名加密，看起来应该最后调用，生成签名串
                .sign(algorithm)
        return jwt
    }

    fun verifyToken(token: String): Boolean {
        val algorithm = Algorithm.RSA256(MyRSAKeyProvider())
        val verify = JWT.require(algorithm).withAudience(AUDIENCE)
                .withJWTId(JWT_ID)
                .withIssuer(ISSUER)
                .acceptExpiresAt(leeway)
                .build()
        verify.verify(token)
        return true
    }

    fun putToken(resp: HttpServletResponse, token: String) {
        resp.addHeader("token", token)
    }

    fun parserIdformToken(token: String):Int{
        val jwt = JWT.decode(token)
        return jwt.getClaim(USER_ID).asInt()

    }
}