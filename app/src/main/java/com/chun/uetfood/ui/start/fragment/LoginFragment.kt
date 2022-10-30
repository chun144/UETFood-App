package com.chun.uetfood.ui.start.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.chun.uetfood.R
import com.chun.uetfood.common.SharedPreferenceCommon
import com.chun.uetfood.databinding.FragmentLoginBinding
import com.chun.uetfood.ui.base.fragment.BaseFragment
import com.chun.uetfood.ui.start.StartActivity
import com.chun.uetfood.viewmodel.LoginViewModel

class LoginFragment : BaseFragment(), View.OnClickListener {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        viewModel = LoginViewModel()
        binding.data = viewModel

        binding.btnLogin.setOnClickListener(this)
        binding.tvRegister.setOnClickListener(this)
        registerLiveData()
        return binding.root
    }

    private fun registerLiveData() {
        viewModel.loginData.observe(viewLifecycleOwner, Observer {

            SharedPreferenceCommon.saveUserToken(requireContext(), it.access_token)
            SharedPreferenceCommon.saveUser(requireContext(), it.username, it.nickname, it.role)

            (requireActivity() as StartActivity).openMain()

        })

        viewModel.errorResponse.observe(viewLifecycleOwner, Observer {
            if (it.error_code == 400) {
                binding.tvError.text = "Tên tài khoản hoặc mật khẩu không chính xác"
            } else {
                binding.tvError.text = "Đã có lỗi xảy ra vui lòng thử lại sau"
            }
        })
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_login -> {
                if (binding.edtUsername.text.toString() != "" && binding.edtPassword.text.toString() != "") {
                    viewModel.login(
                        binding.edtUsername.text.toString(),
                        binding.edtPassword.text.toString(),
                    )
                } else {
                    binding.tvError.text = "Vui lòng nhập tên tài khoản và mật khẩu"
                }
            }
            R.id.tv_register -> {
                (requireActivity() as StartActivity).openRegisterFragment()
            }
        }
    }
}