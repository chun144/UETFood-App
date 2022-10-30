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
import com.chun.uetfood.adapter.CartAdapter
import com.chun.uetfood.common.SharedPreferenceCommon
import com.chun.uetfood.databinding.FragmentCartBinding
import com.chun.uetfood.model.order.Food
import com.chun.uetfood.model.order.OrderComposition
import com.chun.uetfood.model.response.FoodResponse
import com.chun.uetfood.ui.base.fragment.BaseFragment
import com.chun.uetfood.ui.main.MainActivity
import com.chun.uetfood.viewmodel.CartViewModel

class CartFragment: BaseFragment(), CartAdapter.ICartAdapter, View.OnClickListener {
    private lateinit var binding: FragmentCartBinding
    private lateinit var cartViewModel : CartViewModel
    private val foodResponses = mutableListOf<FoodResponse>()
    private val foodResponsesFull = mutableListOf<FoodResponse>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        cartViewModel = CartViewModel()
        binding.data = cartViewModel
        binding.rcFoodCart.adapter = CartAdapter(this)
        binding.rcFoodCart.layoutManager = LinearLayoutManager(requireContext())
        binding.ibBack.setOnClickListener(this)
        binding.ibSearch.setOnClickListener(this)
        binding.ibOrderFood.setOnClickListener(this)
        register()
        val user = SharedPreferenceCommon.readUser(requireContext())
        cartViewModel.getFoodCart(user.username)
        binding.refresh.setOnRefreshListener {
            cartViewModel.getFoodCart(user.username)
        }
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun register(){
        cartViewModel.foodData.observe(viewLifecycleOwner, Observer {
            foodResponses.clear()
            foodResponses.addAll(it)
            foodResponsesFull.clear()
            foodResponsesFull.addAll(it)
            binding.rcFoodCart.adapter?.notifyDataSetChanged()
        })

        cartViewModel.isLoading.observe(viewLifecycleOwner, Observer {
            binding.refresh.isRefreshing = it
        })

        cartViewModel.deleteData.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), "Xóa món ăn trong giỏ hàng thành công", Toast.LENGTH_SHORT).show()
            val user = SharedPreferenceCommon.readUser(requireContext())
            cartViewModel.getFoodCart(user.username)
        })

        cartViewModel.errorResponse.observe(viewLifecycleOwner, Observer {
            if (it.error_code == 401) {
                Toast.makeText(requireContext(), "Đã hết phiên làm việc, vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show()
                (requireActivity() as MainActivity).openStart()
            } else if (it.error_code == 400 || it.error_code == 404) {
                Toast.makeText(requireContext(), "Dữ liệu không hợp lệ, vui lòng thử lại sau", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(),"Đã có lỗi xảy ra vui lòng thử lại sau", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun getCount(): Int {
        return this.foodResponses.size
    }

    override fun getCart(position: Int): FoodResponse {
        return this.foodResponses[position]
    }

    override fun onClickButtonOrderItem(position: Int) {
        var check = true
        for (food in (requireActivity() as MainActivity).listOrderComposition) {
            if (food.food.id == getCart(position).id) {
                Toast.makeText(requireContext(),"Món ăn đã có trong hóa đơn", Toast.LENGTH_SHORT).show()
                check = false
                break
            }
        }
        if (check) {
            val food = Food(getCart(position).id, getCart(position).name, getCart(position).image, getCart(position).description, getCart(position).price)
            val foodOrderComposition = OrderComposition(food, 1, getCart(position).price)
            (requireActivity() as MainActivity).listOrderComposition.add(foodOrderComposition)
            Toast.makeText(requireContext(),"Đã thêm món ăn vào hóa đơn", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClickButtonRemoveItem(position: Int) {
        val user = SharedPreferenceCommon.readUser(requireContext())
        cartViewModel.deleteCart(user.username, getCart(position).id)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClick(view: View) {
        when (view.id) {
            R.id.ib_back -> {
                onBackPressForFragment()
            }
            R.id.ib_search -> {
                val listFood = mutableListOf<FoodResponse>()
                for (food in foodResponsesFull) {
                    if (food.name.contains(binding.edtSearch.text.toString())) {
                        listFood.add(food)
                    }
                }
                foodResponses.clear()
                foodResponses.addAll(listFood)
                binding.rcFoodCart.adapter?.notifyDataSetChanged()
            }
            R.id.ib_orderFood -> {
                (requireActivity() as MainActivity).openFoodOrderFragment(true)
            }
        }
    }

    override fun onBackPressForFragment() {
        (requireActivity() as MainActivity).openHomeFragment()
    }
}