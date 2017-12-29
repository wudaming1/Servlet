package com.aries.servlet.bean



/**
 * @param status 0:请求成功
 *
 */

data class ResponseBean(var status: Int = ResultCode.SUCCESS
                        , var data: Any? = null
                        , var error: ErrorBean? = null)

object ResultCode {
    val SUCCESS = 0
    val FAIL = -1
}


data class ErrorBean(var code: Int = ErrorCode.UNKNOWN,
                     var data: String = "",
                     var message: String = "")


object ErrorCode {


    val UNKNOWN = 0
    val PARAM_ERR = 1
    val DATABASE_ERR = 2
    val TOKEN_INVALID = 3

}