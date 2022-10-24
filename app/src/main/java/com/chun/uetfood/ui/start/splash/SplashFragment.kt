package com.chun.uetfood.ui.start.splash

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chun.uetfood.databinding.FragmentSplashBinding
import com.chun.uetfood.ui.base.fragment.BaseFragment
import com.chun.uetfood.ui.start.StartActivity
import com.chun.uetfood.common.SharedPreferenceCommon

class SplashFragment : BaseFragment() {
    private lateinit var binding: FragmentSplashBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSplashBinding.inflate(inflater, container, false)
        Handler().postDelayed({
            val token = SharedPreferenceCommon.readUserToken(requireContext())
            if ("".equals(token)){
                (requireActivity() as StartActivity).openLoginFragment()
            } else {
                (requireActivity() as StartActivity).openMain()
            }

        }, 2500)
        return binding.root

    }
}