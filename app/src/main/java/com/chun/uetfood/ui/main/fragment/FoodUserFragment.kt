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
import com.chun.uetfood.adapter.FoodUserAdapter
import com.chun.uetfood.common.SharedPreferenceCommon
import com.chun.uetfood.databinding.FragmentFoodUserBinding
import com.chun.uetfood.model.order.Food
import com.chun.uetfood.model.order.OrderComposition
import com.chun.uetfood.model.response.FoodResponse
import com.chun.uetfood.ui.base.fragment.BaseFragment
import com.chun.uetfood.ui.main.MainActivity
import com.chun.uetfood.viewmodel.FoodUserViewModel

class FoodUserFragment: BaseFragment(), FoodUserAdapter.IFoodUserAdapter, View.OnClickListener {
    private lateinit var binding: FragmentFoodUserBinding
    private lateinit var foodUserViewModel : FoodUserViewModel
    private val foodResponses = mutableListOf<FoodResponse>()
    private val foodResponsesFull = mutableListOf<FoodResponse>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFoodUserBinding.inflate(inflater, container, false)
        foodUserViewModel = FoodUserViewModel()
        binding.data = foodUserViewModel
        binding.rcFoodUser.adapter = FoodUserAdapter(this)
        binding.rcFoodUser.layoutManager = LinearLayoutManager(requireContext())
        binding.ibBack.setOnClickListener(this)
        binding.ibSearch.setOnClickListener(this)
        binding.ibOrderFood.setOnClickListener(this)
        register()
        foodUserViewModel.getAllFood()
        initViews()
        return binding.root
    }

    private fun initViews(){
        binding.refresh.setOnRefreshListener {
            foodUserViewModel.getAllFood()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun register(){
        foodUserViewModel.foodData.observe(viewLifecycleOwner, Observer {
            foodResponses.clear()
            foodResponses.addAll(it)
            foodResponsesFull.clear()
            foodResponsesFull.addAll(it)
            binding.rcFoodUser.adapter?.notifyDataSetChanged()
        })

        foodUserViewModel.isLoading.observe(viewLifecycleOwner, Observer {
            binding.refresh.isRefreshing = it
        })

        foodUserViewModel.cartData.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(),"Đã thêm món ăn vào giỏ hàng", Toast.LENGTH_SHORT).show()
        })

        foodUserViewModel.errorResponse.observe(viewLifecycleOwner, Observer {
            if (it.error_code == 401) {
                Toast.makeText(requireContext(), "Đã hết phiên làm việc, vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show()
                (requireActivity() as MainActivity).openStart()
            } else if (it.error_code == 409) {
                Toast.makeText(requireContext(), "Món ăn đã có trong giỏ hàng", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(),"Đã có lỗi xảy ra vui lòng thử lại sau", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun getCount(): Int {
        return this.foodResponses.size
    }

    override fun getFoodUser(position: Int): FoodResponse {
        return this.foodResponses[position]
    }

    override fun onClickButtonOrderItem(position: Int) {
        var check = true
        for (food in (requireActivity() as MainActivity).listOrderComposition) {
            if (food.food.id == getFoodUser(position).id) {
                Toast.makeText(requireContext(),"Món ăn đã có trong hóa đơn", Toast.LENGTH_SHORT).show()
                check = false
                break
            }
        }
        if (check) {
            val food = Food(getFoodUser(position).id, getFoodUser(position).name, getFoodUser(position).image, getFoodUser(position).description, getFoodUser(position).price)
            val foodOrderComposition = OrderComposition(food, 1, getFoodUser(position).price)
            (requireActivity() as MainActivity).listOrderComposition.add(foodOrderComposition)
            Toast.makeText(requireContext(),"Đã thêm món ăn vào hóa đơn", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClickButtonCartItem(position: Int) {
        val user = SharedPreferenceCommon.readUser(requireContext())
        foodUserViewModel.addCart(user.username, getFoodUser(position).id)
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
                binding.rcFoodUser.adapter?.notifyDataSetChanged()
            }
            R.id.ib_orderFood -> {
                (requireActivity() as MainActivity).openFoodOrderFragment(false)
            }
        }
    }

    override fun onBackPressForFragment() {
        (requireActivity() as MainActivity).openHomeFragment()
    }
}