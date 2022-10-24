package com.chun.uetfood.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chun.uetfood.R
import com.chun.uetfood.databinding.FragmentHomeBinding
import com.chun.uetfood.ui.base.fragment.BaseFragment
import com.chun.uetfood.common.SharedPreferenceCommon

class HomeFragment: BaseFragment(), View.OnClickListener {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.btnLogout.setOnClickListener(this)
        initData()
        return binding.root
    }

    private fun initData() {
        val user = SharedPreferenceCommon.readUser(requireContext())
        binding.tvNickname.text = user.nickname
        if (user.role == "admin") {
            binding.avatar.setImageResource(R.drawable.admin)

            binding.iv1.setImageResource(R.drawable.menu)
            binding.function1.text = "Quản lý đồ ăn"
            binding.iv1.setOnClickListener(this)
            binding.iv1.isEnabled = true

            binding.iv2.setImageResource(R.drawable.order)
            binding.function2.text = "Quản lý hóa đơn"
            binding.iv2.setOnClickListener(this)
            binding.iv2.isEnabled = true

            binding.iv3.setImageResource(R.drawable.mail)
            binding.function3.text = "Trao đổi"
            binding.iv3.setOnClickListener(this)
            binding.iv3.isEnabled = true

            binding.iv4.setImageResource(R.drawable.comment)
            binding.function4.text = "Phản hồi"
            binding.iv4.setOnClickListener(this)
            binding.iv4.isEnabled = true

            binding.iv5.isEnabled = false

        } else if (user.role == "user") {
            binding.avatar.setImageResource(R.drawable.user)

            binding.iv1.setImageResource(R.drawable.menu)
            binding.function1.text = "Đặt đồ ăn"
            binding.iv1.setOnClickListener(this)
            binding.iv1.isEnabled = true

            binding.iv2.setImageResource(R.drawable.order)
            binding.function2.text = "Lịch sử đặt hàng"
            binding.iv2.setOnClickListener(this)
            binding.iv2.isEnabled = true

            binding.iv3.setImageResource(R.drawable.mail)
            binding.function3.text = "Trao đổi"
            binding.iv3.setOnClickListener(this)
            binding.iv3.isEnabled = true

            binding.iv4.setImageResource(R.drawable.comment)
            binding.function4.text = "Phản hồi"
            binding.iv4.setOnClickListener(this)
            binding.iv4.isEnabled = true

            binding.iv5.setImageResource(R.drawable.cart)
            binding.function5.text = "Giỏ hàng"
            binding.iv5.setOnClickListener(this)
            binding.iv5.isEnabled = true

        } else if (user.role == "shipper") {
            binding.avatar.setImageResource(R.drawable.shipper)

            binding.iv1.setImageResource(R.drawable.menu)
            binding.function1.text = "Nhận đơn hàng"
            binding.iv1.setOnClickListener(this)
            binding.iv1.isEnabled = true

            binding.iv2.setImageResource(R.drawable.order)
            binding.function2.text = "Lịch sử giao hàng"
            binding.iv2.setOnClickListener(this)
            binding.iv2.isEnabled = true

            binding.iv3.isEnabled = false
            binding.iv4.isEnabled = false
            binding.iv5.isEnabled = false
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_logout -> {
                (requireActivity() as MainActivity).openStart()
            }
        }
    }
}