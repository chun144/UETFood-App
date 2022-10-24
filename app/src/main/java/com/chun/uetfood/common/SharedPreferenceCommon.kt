package com.chun.uetfood.common

import android.content.Context
import com.chun.uetfood.model.user.User

object SharedPreferenceCommon {

    fun saveUserToken(context: Context, token: String) {
        val share = context.getSharedPreferences("user.xml", Context.MODE_PRIVATE)
        val edt = share.edit()
        edt.putString("TOKEN", token)
        edt.apply()
    }

    fun readUserToken(context: Context): String {
        val token = context.getSharedPreferences("user.xml", Context.MODE_PRIVATE)
            .getString("TOKEN", "")
        if (token == null) {
            return ""
        }
        return token
    }

    fun saveUser(context: Context, username: String, nickname: String, role: String) {
        val share = context.getSharedPreferences("in4user.xml", Context.MODE_PRIVATE)
        val edt = share.edit()
        edt.putString("USERNAME", username)
        edt.putString("NICKNAME", nickname)
        edt.putString("ROLE", role)
        edt.apply()
    }

    fun readUser(context: Context): User {
        val username = context.getSharedPreferences("in4user.xml", Context.MODE_PRIVATE)
            .getString("USERNAME", "")
        val nickname = context.getSharedPreferences("in4user.xml", Context.MODE_PRIVATE)
            .getString("NICKNAME", "")
        val role = context.getSharedPreferences("in4user.xml", Context.MODE_PRIVATE)
            .getString("ROLE", "")

        val user = User()
        user.username = username!!
        user.nickname = nickname!!
        user.role = role!!
        return user
    }
}