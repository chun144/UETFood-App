package com.chun.uetfood.model.request

data class RegisterRequest(val username:String, val password: String, val password2: String, val nickname: String, val role: String) {
}