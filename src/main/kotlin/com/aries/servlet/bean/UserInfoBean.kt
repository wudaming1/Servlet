package com.aries.servlet.bean

data class UserInfoBean(val userName:String,
                        val imgUrl:String? = null,
                        val sex:String?=null,
                        val birthday:Long?=null):BaseBean()