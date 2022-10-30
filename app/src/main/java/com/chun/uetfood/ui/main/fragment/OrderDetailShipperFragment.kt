package com.chun.uetfood.ui.main.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chun.uetfood.R
import com.chun.uetfood.adapter.OrderDetailShipperAdapter
import com.chun.uetfood.databinding.FragmentOrderDetailShipperBinding
import com.chun.uetfood.model.response.OrderCompositionResponse
import com.chun.uetfood.model.response.OrderResponse
import com.chun.uetfood.ui.base.fragment.BaseFragment
import com.chun.uetfood.ui.main.MainActivity
import com.chun.uetfood.viewmodel.OrderDetailShipperViewModel


class OrderDetailShipperFragment: BaseFragment(), OrderDetailShipperAdapter.IOrderDetailShipperAdapter, View.OnClickListener {
    private lateinit var binding: FragmentOrderDetailShipperBinding
    private lateinit var orderDetailShipperViewModel : OrderDetailShipperViewModel
    private var orderDetailShipperResponse = OrderResponse()
    private var orderId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderDetailShipperBinding.inflate(inflater, container, false)
        orderDetailShipperViewModel = OrderDetailShipperViewModel()
        binding.data2 = orderDetailShipperViewModel
        binding.rcOrderFoodShipper.adapter = OrderDetailShipperAdapter(this)
        binding.rcOrderFoodShipper.layoutManager = LinearLayoutManager(requireContext())
        binding.ibBack.setOnClickListener(this)
        initData()
        register()
        orderDetailShipperViewModel.getOrder(orderId)
        return binding.root
    }

    private fun initData() {
        if (arguments != null) {
            orderId = requireArguments().getInt("orderId")
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun register() {
        orderDetailShipperViewModel.orderData.observe(viewLifecycleOwner, Observer {
            orderDetailShipperResponse = it
            binding.data = orderDetailShipperResponse
            if (orderDetailShipperResponse.status == "Đang giao hàng") {
                binding.btnShipped.setBackgroundResource(R.drawable.bg_button_fix)
                binding.btnShipped.text = "Đã giao hàng"
                binding.btnShipped.setOnClickListener(this)

                binding.btnCanceled.setBackgroundResource(R.drawable.bg_button_logout)
                binding.btnCanceled.text = "Đã hủy đơn hàng"
                binding.btnCanceled.setOnClickListener(this)
            }
            binding.rcOrderFoodShipper.adapter?.notifyDataSetChanged()
        })

        orderDetailShipperViewModel.putData.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), "Xác nhận thành công", Toast.LENGTH_SHORT).show()
            (requireActivity() as MainActivity).openOrderFragment()
        })

        orderDetailShipperViewModel.errorResponse.observe(viewLifecycleOwner, Observer {
            if (it.error_code == 401) {
                Toast.makeText(requireContext(), "Đã hết phiên làm việc, vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show()
                (requireActivity() as MainActivity).openStart()
            } else if (it.error_code == 400 || it.error_code == 404) {
                Toast.makeText(requireContext(),"Dữ liệu không hợp lệ, vui lòng thử lại sau", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(),"Đã có lỗi xảy ra vui lòng thử lại sau", Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun getCount(): Int {
        return this.orderDetailShipperResponse.listOrderComposition.size
    }

    override fun getOrderDetailShipper(position: Int): OrderCompositionResponse {
        return this.orderDetailShipperResponse.listOrderComposition[position]
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClick(view: View) {
        when (view.id) {
            R.id.ib_back -> {
                onBackPressForFragment()
            }
            R.id.btn_shipped -> {
                orderDetailShipperViewModel.putStatusOrder(orderId, "shipped")
            }
            R.id.btn_canceled -> {
                orderDetailShipperViewModel.putStatusOrder(orderId, "canceled")
            }
        }
    }

    override fun onBackPressForFragment() {
        (requireActivity() as MainActivity).openOrderFragment()
    }
}