package com.chun.uetfood.ui.base.fragment

import androidx.fragment.app.Fragment
import com.chun.uetfood.ui.base.activity.BaseActivity

open abstract class BaseFragment : Fragment(){

    open fun onBackPress(){
        (requireActivity() as BaseActivity).onBackPressRoot()
    }
}