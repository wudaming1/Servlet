package com.aries.servlet.bean



/**
 * @param resultCode 0:请求正常
 *
 */

data class ResponseBean(var resultCode: Int = HttpResultCode.FAIL
                        , var data:String = ""
                        , var message:String = "")