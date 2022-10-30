package com.chun.uetfood.common

import android.app.Application
import android.content.Context

class CommonApp : Application() {
    companion object {
        private var context: Context? = null
        fun getContextApp(): Context {
            return context!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}