package com.chun.uetfood.ui.start.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import com.chun.uetfood.R
import com.chun.uetfood.databinding.FragmentRegisterBinding
import com.chun.uetfood.ui.base.fragment.BaseFragment
import com.chun.uetfood.ui.start.StartActivity
import com.chun.uetfood.viewmodel.RegisterViewModel

class RegisterFragment : BaseFragment(), View.OnClickListener {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var dialog : Dialog
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.btnRegister.setOnClickListener(this)
        binding.tvLogin.setOnClickListener(this)
        registerViewModel = RegisterViewModel()
        registerLiveData()
        binding.data = registerViewModel
        return binding.root
    }


    private fun registerLiveData() {
        registerViewModel.registerData.observe(viewLifecycleOwner) {
            openDialog()
        }

        registerViewModel.errorResponse.observe(viewLifecycleOwner, Observer {
            if (it.error_code == 409) {
                binding.tvError.text = "Tên tài khoản đã tồn tại"
            } else if (it.error_code == 400){
                binding.tvError.text = "Nhập lại mật khẩu chưa chính xác"
            } else {
                binding.tvError.text = "Đã có lỗi xảy ra vui lòng thử lại sau"
            }
        })
    }

    private fun openDialog() {
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_register_done)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        var buttonLogin: AppCompatButton = dialog.findViewById(R.id.btn_login)
        buttonLogin.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_register -> {
                if (binding.edtUsername.text.toString() != "" && binding.edtPassword.text.toString() != "" &&
                binding.edtPassword2.text.toString() != "" && binding.edtNickname.text.toString() != "") {
                    if (binding.edtPassword.text.toString() != binding.edtPassword2.text.toString()) {
                        binding.tvError.text = "Nhập lại mật khẩu chưa chính xác"
                    } else {
                        registerViewModel.register(
                            binding.edtUsername.text.toString(),
                            binding.edtPassword.text.toString(),
                            binding.edtPassword2.text.toString(),
                            binding.edtNickname.text.toString(),
                            "user"
                        )
                    }
                } else {
                    binding.tvError.text = "Vui lòng nhập đầy đủ thông tin"
                }
            }

            R.id.btn_login-> {
                dialog.cancel()
                (requireActivity() as StartActivity).openLoginFragment()
            }
            R.id.tv_login -> {
                (requireActivity() as StartActivity).openLoginFragment()
            }
        }
    }
}