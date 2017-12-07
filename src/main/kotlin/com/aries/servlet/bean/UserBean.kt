package com.aries.servlet.bean

data class UserBean(val id: Int,
                    val userName: String,
                    val password: String) : BaseBean()