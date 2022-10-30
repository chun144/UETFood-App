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
import com.chun.uetfood.adapter.OrderDetailAdapter
import com.chun.uetfood.databinding.FragmentOrderDetailBinding
import com.chun.uetfood.model.response.OrderCompositionResponse
import com.chun.uetfood.model.response.OrderResponse
import com.chun.uetfood.ui.base.fragment.BaseFragment
import com.chun.uetfood.ui.main.MainActivity
import com.chun.uetfood.viewmodel.OrderDetailViewModel


class OrderDetailFragment: BaseFragment(), OrderDetailAdapter.IOrderDetailAdapter, View.OnClickListener {
    private lateinit var binding: FragmentOrderDetailBinding
    private lateinit var orderDetailViewModel : OrderDetailViewModel
    private var orderDetailResponse = OrderResponse()
    private var orderId: Int = 0
    private var checkShip : Boolean? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderDetailBinding.inflate(inflater, container, false)
        orderDetailViewModel = OrderDetailViewModel()
        binding.data2 = orderDetailViewModel
        binding.rcOrderFood.adapter = OrderDetailAdapter(this)
        binding.rcOrderFood.layoutManager = LinearLayoutManager(requireContext())
        binding.ibBack.setOnClickListener(this)
        initData()
        register()
        orderDetailViewModel.getOrder(orderId)
        return binding.root
    }

    private fun initData() {
        if (arguments != null) {
            orderId = requireArguments().getInt("orderId")
            checkShip = requireArguments().getBoolean("checkShip")
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun register() {
        orderDetailViewModel.orderData.observe(viewLifecycleOwner, Observer {
            orderDetailResponse = it
            binding.data = orderDetailResponse
            binding.rcOrderFood.adapter?.notifyDataSetChanged()
        })

        orderDetailViewModel.errorResponse.observe(viewLifecycleOwner, Observer {
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
        return this.orderDetailResponse.listOrderComposition.size
    }

    override fun getOrderDetail(position: Int): OrderCompositionResponse {
        return this.orderDetailResponse.listOrderComposition[position]
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClick(view: View) {
        when (view.id) {
            R.id.ib_back -> {
                onBackPressForFragment()
            }
        }
    }

    override fun onBackPressForFragment() {
        if (checkShip!!) {
            (requireActivity() as MainActivity).openOrderShipperFragment()
        } else {
            (requireActivity() as MainActivity).openOrderFragment()
        }
    }
}