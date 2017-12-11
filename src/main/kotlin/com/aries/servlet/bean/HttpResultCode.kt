package com.aries.servlet.bean



/**
 * http请求状态表
 */
object HttpResultCode{
    val TOKEN_INVALID = -2
    val FAIL = -1
    val SUCCESS = 0
    val PARAM_ERR = 1
    val DATABASE_ERR = 2

}