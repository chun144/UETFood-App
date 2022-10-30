package com.chun.uetfood.ui.base.fragment

import androidx.fragment.app.Fragment
import com.chun.uetfood.ui.base.activity.BaseActivity

open abstract class BaseFragment : Fragment(){

    open fun onBackPressForFragment(){
        if (activity is BaseActivity){
            (activity as BaseActivity).onBackPressRoot()
        }
    }

    open fun onFinish(){
        if ( activity is BaseActivity){
            (activity as BaseActivity).onFinish()
        }
    }
}