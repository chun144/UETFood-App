package com.chun.uetfood.ui.main

import android.content.Intent
import android.os.Bundle
import com.chun.uetfood.R
import com.chun.uetfood.ui.base.activity.BaseActivity
import com.chun.uetfood.ui.start.StartActivity
import com.chun.uetfood.common.SharedPreferenceCommon

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .add(R.id.main, HomeFragment(), HomeFragment::class.java.name)
            .commit()
    }

    fun openStart() {
        SharedPreferenceCommon.saveUserToken(this, "")
        val intent = Intent()
        intent.setClass(this, StartActivity::class.java)
        intent.putExtra("TYPE","OPEN_LOGIN")
        startActivity(intent)
        finish()
    }
}