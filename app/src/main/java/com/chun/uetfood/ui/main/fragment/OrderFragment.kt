package com.chun.uetfood.ui.main.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chun.uetfood.R
import com.chun.uetfood.adapter.OrderAdapter
import com.chun.uetfood.common.SharedPreferenceCommon
import com.chun.uetfood.databinding.FragmentOrderBinding
import com.chun.uetfood.model.response.OrderResponse
import com.chun.uetfood.ui.base.fragment.BaseFragment
import com.chun.uetfood.ui.main.MainActivity
import com.chun.uetfood.viewmodel.OrderViewModel


class OrderFragment : BaseFragment(), OrderAdapter.IOrderAdapter, View.OnClickListener {
    private lateinit var binding: FragmentOrderBinding
    private lateinit var orderViewModel: OrderViewModel
    private val orderResponses = mutableListOf<OrderResponse>()
    private val orderResponsesFull = mutableListOf<OrderResponse>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderBinding.inflate(inflater, container, false)
        orderViewModel = OrderViewModel()
        binding.data = orderViewModel
        binding.rcOrder.adapter = OrderAdapter(this)
        binding.rcOrder.layoutManager = LinearLayoutManager(requireContext())
        binding.ibBack.setOnClickListener(this)
        binding.ibSearch.setOnClickListener(this)
        binding.ibSearchType.setOnClickListener(this)
        register()
        initData()
        initViews()
        return binding.root
    }

    private fun initData() {
        val user = SharedPreferenceCommon.readUser(requireContext())
        when (user.role) {
            "admin" -> {
                binding.tvTitle.text = "Quản lý hóa đơn"
                orderViewModel.getAllOrder()
            }
            "user" -> {
                binding.tvTitle.text = "Lịch sử đặt hàng"
                orderViewModel.getOrderUser(user.username)
            }
            "shipper" -> {
                binding.tvTitle.text = "Lịch sử giao hàng"
                orderViewModel.getOrderShipper(user.username)
            }
        }
    }

    private fun initViews() {
        val user = SharedPreferenceCommon.readUser(requireContext())
        binding.refresh.setOnRefreshListener {
            when (user.role) {
                "admin" -> {
                    orderViewModel.getAllOrder()
                }
                "user" -> {
                    orderViewModel.getOrderUser(user.username)
                }
                "shipper" -> {
                    orderViewModel.getOrderShipper(user.username)
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun register() {
        orderViewModel.orderData.observe(viewLifecycleOwner, Observer {
            orderResponses.clear()
            orderResponses.addAll(it)
            orderResponsesFull.clear()
            orderResponsesFull.addAll(it)
            binding.rcOrder.adapter?.notifyDataSetChanged()
        })

        orderViewModel.isLoading.observe(viewLifecycleOwner, Observer {
            binding.refresh.isRefreshing = it
        })

        orderViewModel.errorResponse.observe(viewLifecycleOwner, Observer {
            if (it.error_code == 401) {
                Toast.makeText(
                    requireContext(),
                    "Đã hết phiên làm việc, vui lòng đăng nhập lại",
                    Toast.LENGTH_SHORT
                ).show()
                (requireActivity() as MainActivity).openStart()
            } else if (it.error_code == 400 || it.error_code == 404) {
                Toast.makeText(
                    requireContext(),
                    "Dữ liệu không hợp lệ, vui lòng thử lại sau",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Đã có lỗi xảy ra vui lòng thử lại sau",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

    override fun getCount(): Int {
        return this.orderResponses.size
    }

    override fun getOrder(position: Int): OrderResponse {
        return this.orderResponses[position]
    }

    override fun onClickItem(position: Int) {
        val user = SharedPreferenceCommon.readUser(requireContext())
        if (user.role == "admin" || user.role == "user") {
            (requireActivity() as MainActivity).openOrderDetailFragment(getOrder(position).id, false)
        } else if (user.role == "shipper") {
            (requireActivity() as MainActivity).openOrderDetailShipperFragment(getOrder(position).id)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun searchByStatus() {
        val listOrder = mutableListOf<OrderResponse>()
        for (order in orderResponsesFull) {
            if (order.status == binding.edtSearch.text.toString()) {
                listOrder.add(order)
            }
        }
        orderResponses.clear()
        orderResponses.addAll(listOrder)
        binding.rcOrder.adapter?.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClick(view: View) {
        when (view.id) {
            R.id.ib_back -> {
                onBackPressForFragment()
            }
            R.id.ib_search -> {
                if (binding.edtSearch.text.toString() != "") {
                    val listOrder = mutableListOf<OrderResponse>()
                    if (binding.tvSearchType.text.toString() == "Đơn hàng số") {
                        for (order in orderResponsesFull) {
                            if (order.id.toString() == (binding.edtSearch.text.toString())) {
                                listOrder.add(order)
                            }
                        }
                    } else if (binding.tvSearchType.text.toString() == "Trạng thái") {
                        for (order in orderResponsesFull) {
                            if (order.status.contains(binding.edtSearch.text.toString())) {
                                listOrder.add(order)
                            }
                        }
                    } else if (binding.tvSearchType.text.toString() == "Ngày đặt") {
                        for (order in orderResponsesFull) {
                            if (order.date.contains(binding.edtSearch.text.toString())) {
                                listOrder.add(order)
                            }
                        }
                    }
                    orderResponses.clear()
                    orderResponses.addAll(listOrder)
                    binding.rcOrder.adapter?.notifyDataSetChanged()
                }
            }
            R.id.ib_searchType -> {
                val popupMenu = PopupMenu(requireContext(), binding.ibSearchType)
                popupMenu.menuInflater.inflate(R.menu.context_menu, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.context_id -> {
                            binding.tvSearchType.text = "Đơn hàng số"
                            true
                        }
                        R.id.context_status -> {
                            binding.tvSearchType.text = "Trạng thái"
                            true
                        }
                        R.id.context_status_ordered -> {
                            binding.edtSearch.setText("Đã đặt hàng")
                            searchByStatus()
                            true
                        }
                        R.id.context_status_shipping -> {
                            binding.edtSearch.setText("Đang giao hàng")
                            searchByStatus()
                            true
                        }
                        R.id.context_status_shipped -> {
                            binding.edtSearch.setText("Đã giao hàng")
                            searchByStatus()
                            true
                        }
                        R.id.context_status_canceled -> {
                            binding.edtSearch.setText("Đã hủy đơn hàng")
                            searchByStatus()
                            true
                        }
                        R.id.context_date -> {
                            binding.tvSearchType.text = "Ngày đặt"
                            true
                        }
                        else -> {
                            true
                        }
                    }
                }
                popupMenu.show()
            }
        }
    }

    override fun onBackPressForFragment() {
        (requireActivity() as MainActivity).openHomeFragment()
    }
}