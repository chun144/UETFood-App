package com.chun.uetfood.ui.main.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chun.uetfood.R
import com.chun.uetfood.adapter.FoodOrderAdapter
import com.chun.uetfood.common.SharedPreferenceCommon
import com.chun.uetfood.databinding.FragmentFoodOrderBinding
import com.chun.uetfood.model.order.OrderComposition
import com.chun.uetfood.ui.base.fragment.BaseFragment
import com.chun.uetfood.ui.main.MainActivity
import com.chun.uetfood.viewmodel.FoodOrderViewModel

class FoodOrderFragment: BaseFragment(), FoodOrderAdapter.IFoodOrderAdapter, View.OnClickListener {
    private lateinit var binding: FragmentFoodOrderBinding
    private lateinit var foodOrderViewModel : FoodOrderViewModel
    private var listFood = mutableListOf<OrderComposition>()
    private lateinit var dialogOrder : Dialog
    private lateinit var dialogCancel : Dialog
    private var checkCart : Boolean? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFoodOrderBinding.inflate(inflater, container, false)
        foodOrderViewModel = FoodOrderViewModel()
        binding.data = foodOrderViewModel
        initData()
        binding.rcFoodOrder.adapter = FoodOrderAdapter(this)
        binding.rcFoodOrder.layoutManager = LinearLayoutManager(requireContext())
        binding.ibBack.setOnClickListener(this)
        binding.btnOrder.setOnClickListener(this)
        binding.btnCancel.setOnClickListener(this)
        registerLiveData()
        return binding.root
    }

    private fun initData() {
        if (arguments != null) {
            checkCart = requireArguments().getBoolean("checkCart")
        }

        listFood = (requireActivity() as MainActivity).listOrderComposition
        updateTotalPriceOrder()
        binding.edtPhone.setText((requireActivity() as MainActivity).phone)
        binding.edtAddress.setText((requireActivity() as MainActivity).address)
        binding.edtNote.setText((requireActivity() as MainActivity).note)
    }

    private fun registerLiveData() {
        foodOrderViewModel.foodOrderData.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(),"Đặt hàng thành công", Toast.LENGTH_SHORT).show()
            clearOrder()
            (requireActivity() as MainActivity).openFoodUserFragment()
        })

        foodOrderViewModel.errorResponse.observe(viewLifecycleOwner, Observer {
            if (it.error_code == 400 || it.error_code == 404) {
                Toast.makeText(requireContext(),"Dữ liệu không hợp lệ, vui lòng thử lại sau", Toast.LENGTH_SHORT).show()
            } else if (it.error_code == 401) {
                Toast.makeText(requireContext(),"Đã hết phiên làm việc, vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show()
                (requireActivity() as MainActivity).openStart()
            } else {
                Toast.makeText(requireContext(),"Đã có lỗi xảy ra vui lòng thử lại sau", Toast.LENGTH_SHORT).show()
            }
            })
        }

    override fun getCount(): Int {
        return this.listFood.size
    }

    override fun getFoodOrder(position: Int): OrderComposition {
        return this.listFood[position]
    }

    private fun updateTotalPriceOrder() {
        (requireActivity() as MainActivity).totalPriceOrder = 0
        for (food in (requireActivity() as MainActivity).listOrderComposition) {
            (requireActivity() as MainActivity).totalPriceOrder += food.totalPrice
        }
        binding.totalPriceOrder.text = (requireActivity() as MainActivity).totalPriceOrder.toString()
    }

    private fun clearOrder() {
        (requireActivity() as MainActivity).listOrderComposition.clear()
        (requireActivity() as MainActivity).phone = ""
        (requireActivity() as MainActivity).address = ""
        (requireActivity() as MainActivity).note = ""
        (requireActivity() as MainActivity).totalPriceOrder = 0
    }

    private fun openDialogOrder() {
        dialogOrder = Dialog(requireContext())
        dialogOrder.setContentView(R.layout.dialog_order)
        dialogOrder.setCancelable(false)
        dialogOrder.setCanceledOnTouchOutside(false)
        dialogOrder.show()
        var buttonOrder: AppCompatButton = dialogOrder.findViewById(R.id.btn_confirm)
        var buttonCancel: AppCompatButton = dialogOrder.findViewById(R.id.btn_cancelOrder)
        buttonOrder.setOnClickListener(this)
        buttonCancel.setOnClickListener(this)
    }

    private fun openDialogCancel() {
        dialogCancel = Dialog(requireContext())
        dialogCancel.setContentView(R.layout.dialog_cancel_order)
        dialogCancel.setCancelable(false)
        dialogCancel.setCanceledOnTouchOutside(false)
        dialogCancel.show()
        var buttonCancel: AppCompatButton = dialogCancel.findViewById(R.id.btn_cancelOrderFood)
        var buttonCancelDialog: AppCompatButton = dialogCancel.findViewById(R.id.btn_cancelDialog)
        buttonCancel.setOnClickListener(this)
        buttonCancelDialog.setOnClickListener(this)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClickButtonUpItem(position: Int) {
        (requireActivity() as MainActivity).listOrderComposition[position].quantity += 1
        (requireActivity() as MainActivity).listOrderComposition[position].totalPrice = (requireActivity() as MainActivity).listOrderComposition[position].quantity * (requireActivity() as MainActivity).listOrderComposition[position].food.price
        updateTotalPriceOrder()
        binding.rcFoodOrder.adapter?.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClickButtonDownItem(position: Int) {
        if (listFood[position].quantity > 1) {
            (requireActivity() as MainActivity).listOrderComposition[position].quantity -= 1
            (requireActivity() as MainActivity).listOrderComposition[position].totalPrice = (requireActivity() as MainActivity).listOrderComposition[position].quantity * (requireActivity() as MainActivity).listOrderComposition[position].food.price
            updateTotalPriceOrder()
            binding.rcFoodOrder.adapter?.notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClickButtonRemoveItem(position: Int) {
        (requireActivity() as MainActivity).listOrderComposition.removeAt(position)
        updateTotalPriceOrder()
        binding.rcFoodOrder.adapter?.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClick(view: View) {
        val user = SharedPreferenceCommon.readUser(requireContext())
        when (view.id) {
            R.id.ib_back -> {
                onBackPressForFragment()
            }
            R.id.btn_order -> {
                if (binding.edtPhone.text.toString() != "" && binding.edtAddress.text.toString() != "") {
                    if ((requireActivity() as MainActivity).totalPriceOrder == 0) {
                        Toast.makeText(requireContext(),"Vui lòng chọn món ăn", Toast.LENGTH_SHORT).show()
                    } else {
                       openDialogOrder()
                    }
                } else {
                    Toast.makeText(requireContext(),"Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.btn_cancel -> {
                openDialogCancel()
            }
            R.id.btn_confirm-> {
                dialogOrder.cancel()
                var listOrderComposition = ""
                for (i in (requireActivity() as MainActivity).listOrderComposition) {
                    listOrderComposition += "${i.food.id}-${i.quantity},"
                }
                listOrderComposition = listOrderComposition.substring(0, (listOrderComposition.length - 1))
                if (binding.edtNote.text.toString() != "") {
                    foodOrderViewModel.addOrder(
                        user.username,
                        listOrderComposition,
                        binding.edtAddress.text.toString(),
                        binding.edtPhone.text.toString(),
                        binding.edtNote.text.toString()
                    )
                } else {
                    foodOrderViewModel.addOrder(
                        user.username,
                        listOrderComposition,
                        binding.edtAddress.text.toString(),
                        binding.edtPhone.text.toString(),
                        "."
                    )
                }
            }
            R.id.btn_cancelOrder -> {
                dialogOrder.cancel()
            }
            R.id.btn_cancelOrderFood-> {
                dialogCancel.cancel()
                clearOrder()
                if (checkCart!!) {
                    (requireActivity() as MainActivity).openCartFragment()
                } else {
                    (requireActivity() as MainActivity).openFoodUserFragment()
                }
            }
            R.id.btn_cancelDialog -> {
                dialogCancel.cancel()
            }
        }
    }

    override fun onBackPressForFragment() {
        if (binding.edtPhone.text.toString() != "") {
            (requireActivity() as MainActivity).phone = binding.edtPhone.text.toString()
        }
        if (binding.edtAddress.text.toString() != "") {
            (requireActivity() as MainActivity).address = binding.edtAddress.text.toString()
        }
        if (binding.edtNote.text.toString() != "") {
            (requireActivity() as MainActivity).note = binding.edtNote.text.toString()
        }
        if (checkCart!!) {
            (requireActivity() as MainActivity).openCartFragment()
        } else {
            (requireActivity() as MainActivity).openFoodUserFragment()
        }
    }
}