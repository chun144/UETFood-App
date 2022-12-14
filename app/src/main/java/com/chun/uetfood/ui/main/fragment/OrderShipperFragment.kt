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
import com.chun.uetfood.adapter.OrderShipperAdapter
import com.chun.uetfood.common.SharedPreferenceCommon
import com.chun.uetfood.databinding.FragmentOrderShipperBinding
import com.chun.uetfood.model.response.OrderResponse
import com.chun.uetfood.ui.base.fragment.BaseFragment
import com.chun.uetfood.ui.main.MainActivity
import com.chun.uetfood.viewmodel.OrderShipperViewModel


class OrderShipperFragment : BaseFragment(), OrderShipperAdapter.IOrderShipperAdapter, View.OnClickListener {
    private lateinit var binding: FragmentOrderShipperBinding
    private lateinit var orderShipperViewModel: OrderShipperViewModel
    private val orderShipperResponses = mutableListOf<OrderResponse>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderShipperBinding.inflate(inflater, container, false)
        orderShipperViewModel = OrderShipperViewModel()
        binding.data = orderShipperViewModel
        binding.rcOrderShipper.adapter = OrderShipperAdapter(this)
        binding.rcOrderShipper.layoutManager = LinearLayoutManager(requireContext())
        binding.ibBack.setOnClickListener(this)
        register()
        orderShipperViewModel.getAllOrder()
        initViews()
        return binding.root
    }

    private fun initViews() {
        binding.refresh.setOnRefreshListener {
            orderShipperViewModel.getAllOrder()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun register() {
        orderShipperViewModel.orderData.observe(viewLifecycleOwner, Observer {
            orderShipperResponses.clear()
            for (order in it) {
                if (order.status == "???? ?????t h??ng") {
                    orderShipperResponses.add(order)
                }
            }
            binding.rcOrderShipper.adapter?.notifyDataSetChanged()
        })

        orderShipperViewModel.orderShipperData.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), "Nh???n ????n h??ng th??nh c??ng", Toast.LENGTH_SHORT).show()
            orderShipperViewModel.getAllOrder()
        })

        orderShipperViewModel.isLoading.observe(viewLifecycleOwner, Observer {
            binding.refresh.isRefreshing = it
        })

        orderShipperViewModel.errorResponse.observe(viewLifecycleOwner, Observer {
            if (it.error_code == 401) {
                Toast.makeText(requireContext(), "???? h???t phi??n l??m vi???c, vui l??ng ????ng nh???p l???i", Toast.LENGTH_SHORT).show()
                (requireActivity() as MainActivity).openStart()
            } else if (it.error_code == 400 || it.error_code == 404) {
                Toast.makeText(requireContext(), "D??? li???u kh??ng h???p l???, vui l??ng th??? l???i sau", Toast.LENGTH_SHORT).show()
            } else if (it.error_code == 406) {
                Toast.makeText(requireContext(), "????n h??ng ???? ???????c nh???n giao", Toast.LENGTH_SHORT).show()
            } else if (it.error_code == 409) {
                Toast.makeText(requireContext(), "???? nh???n ????n h??ng", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "???? c?? l???i x???y ra vui l??ng th??? l???i sau", Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun getCount(): Int {
        return this.orderShipperResponses.size
    }

    override fun getOrderShipper(position: Int): OrderResponse {
        return this.orderShipperResponses[position]
    }

    override fun onClickItem(position: Int) {
        (requireActivity() as MainActivity).openOrderDetailFragment(getOrderShipper(position).id, true)
    }

    override fun onClickButtonItem(position: Int) {
        val user = SharedPreferenceCommon.readUser(requireContext())
        orderShipperViewModel.addOrderShipper(user.username, getOrderShipper(position).id)
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
        (requireActivity() as MainActivity).openHomeFragment()
    }
}