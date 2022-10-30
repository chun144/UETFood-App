package com.chun.uetfood.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chun.uetfood.R
import com.chun.uetfood.common.SharedPreferenceCommon
import com.chun.uetfood.databinding.FragmentHomeBinding
import com.chun.uetfood.ui.base.fragment.BaseFragment
import com.chun.uetfood.ui.main.MainActivity

class HomeFragment: BaseFragment(), View.OnClickListener {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.iv6.setOnClickListener(this)
        initData()
        return binding.root
    }

    private fun initData() {
        val user = SharedPreferenceCommon.readUser(requireContext())
        binding.tvNickname.text = user.nickname
        if (user.role == "admin") {
            binding.avatar.setImageResource(R.drawable.admin)

            binding.iv1.setImageResource(R.drawable.baseline_fastfood_white_24dp)
            binding.function1.text = "Quản lý đồ ăn"
            binding.iv1.setOnClickListener(this)
            binding.iv1.isEnabled = true

            binding.iv2.setImageResource(R.drawable.baseline_request_quote_white_24dp)
            binding.function2.text = "Quản lý hóa đơn"
            binding.iv2.setOnClickListener(this)
            binding.iv2.isEnabled = true

            binding.iv3.setImageResource(R.drawable.baseline_mail_white_24dp)
            binding.function3.text = "Trao đổi"
            binding.iv3.setOnClickListener(this)
            binding.iv3.isEnabled = true

            binding.iv4.setImageResource(R.drawable.baseline_star_border_white_24dp)
            binding.function4.text = "Phản hồi"
            binding.iv4.setOnClickListener(this)
            binding.iv4.isEnabled = true

            binding.iv6.setImageResource(R.drawable.baseline_logout_white_24dp)
            binding.function6.text = "Đăng xuất"
            binding.iv6.setOnClickListener(this)
            binding.iv6.isEnabled = true

            binding.iv5.isEnabled = false

        } else if (user.role == "user") {
            binding.avatar.setImageResource(R.drawable.user)

            binding.iv1.setImageResource(R.drawable.baseline_fastfood_white_24dp)
            binding.function1.text = "Đặt đồ ăn"
            binding.iv1.setOnClickListener(this)
            binding.iv1.isEnabled = true

            binding.iv2.setImageResource(R.drawable.baseline_request_quote_white_24dp)
            binding.function2.text = "Lịch sử đặt hàng"
            binding.iv2.setOnClickListener(this)
            binding.iv2.isEnabled = true

            binding.iv3.setImageResource(R.drawable.baseline_shopping_cart_white_24dp)
            binding.function3.text = "Giỏ hàng"
            binding.iv3.setOnClickListener(this)
            binding.iv3.isEnabled = true

            binding.iv4.setImageResource(R.drawable.baseline_mail_white_24dp)
            binding.function4.text = "Trao đổi"
            binding.iv4.setOnClickListener(this)
            binding.iv4.isEnabled = true

            binding.iv5.setImageResource(R.drawable.baseline_star_border_white_24dp)
            binding.function5.text = "Phản hồi"
            binding.iv5.setOnClickListener(this)
            binding.iv5.isEnabled = true

            binding.iv6.setImageResource(R.drawable.baseline_logout_white_24dp)
            binding.function6.text = "Đăng xuất"
            binding.iv6.setOnClickListener(this)
            binding.iv6.isEnabled = true

        } else if (user.role == "shipper") {
            binding.avatar.setImageResource(R.drawable.shipper)

            binding.iv1.setImageResource(R.drawable.baseline_fastfood_white_24dp)
            binding.function1.text = "Nhận đơn hàng"
            binding.iv1.setOnClickListener(this)
            binding.iv1.isEnabled = true

            binding.iv2.setImageResource(R.drawable.baseline_request_quote_white_24dp)
            binding.function2.text = "Lịch sử giao hàng"
            binding.iv2.setOnClickListener(this)
            binding.iv2.isEnabled = true

            binding.iv4.setImageResource(R.drawable.baseline_logout_white_24dp)
            binding.function4.text = "Đăng xuất"
            binding.iv4.setOnClickListener(this)
            binding.iv4.isEnabled = true

            binding.iv3.isEnabled = false
            binding.iv5.isEnabled = false
            binding.iv6.isEnabled = false
        }
    }

    override fun onClick(view: View) {
        val user = SharedPreferenceCommon.readUser(requireContext())
        when (view.id) {
            R.id.iv1 -> {
                if (user.role == "admin") {
                    (requireActivity() as MainActivity).openFoodAdminFragment()
                } else if (user.role == "user") {
                    (requireActivity() as MainActivity).openFoodUserFragment()
                } else if (user.role == "shipper") {
                    (requireActivity() as MainActivity).openOrderShipperFragment()
                }
            }
            R.id.iv2 -> {
                (requireActivity() as MainActivity).openOrderFragment()
            }
            R.id.iv3 -> {
                if (user.role == "user") {
                    (requireActivity() as MainActivity).openCartFragment()
                } else if (user.role == "admin") {
                    (requireActivity() as MainActivity).openCommunicateFragment()
                }
            }
            R.id.iv4 -> {
                if (user.role == "shipper") {
                    (requireActivity() as MainActivity).openStart()
                } else if (user.role == "user") {
                    (requireActivity() as MainActivity).openCommunicateFragment()
                } else if (user.role == "admin") {
                    (requireActivity() as MainActivity).openFeedbackFragment()
                }
            }
            R.id.iv5 -> {
                if (user.role == "user") {
                    (requireActivity() as MainActivity).openFeedbackFragment()
                }
            }
            R.id.iv6 -> {
                if (user.role == "admin" || user.role == "user") {
                    (requireActivity() as MainActivity).openStart()
                }
            }
        }
    }
}