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
import com.chun.uetfood.adapter.FoodAdminAdapter
import com.chun.uetfood.databinding.FragmentFoodAdminBinding
import com.chun.uetfood.model.response.FoodResponse
import com.chun.uetfood.ui.base.fragment.BaseFragment
import com.chun.uetfood.ui.main.MainActivity
import com.chun.uetfood.viewmodel.FoodAdminViewModel

class FoodAdminFragment: BaseFragment(), FoodAdminAdapter.IFoodAdminAdapter, View.OnClickListener {
    private lateinit var binding: FragmentFoodAdminBinding
    private lateinit var foodAdminViewModel : FoodAdminViewModel
    private val foodResponses = mutableListOf<FoodResponse>()
    private val foodResponsesFull = mutableListOf<FoodResponse>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFoodAdminBinding.inflate(inflater, container, false)
        foodAdminViewModel = FoodAdminViewModel()
        binding.data = foodAdminViewModel
        binding.rcFoodAdmin.adapter = FoodAdminAdapter(this)
        binding.rcFoodAdmin.layoutManager = LinearLayoutManager(requireContext())
        binding.ibBack.setOnClickListener(this)
        binding.ibSearch.setOnClickListener(this)
        binding.ibAddFood.setOnClickListener(this)
        register()
        foodAdminViewModel.getAllFood()
        initViews()
        return binding.root
    }

    private fun initViews(){
        binding.refresh.setOnRefreshListener {
            foodAdminViewModel.getAllFood()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun register(){
        foodAdminViewModel.foodData.observe(viewLifecycleOwner, Observer {
            foodResponses.clear()
            foodResponses.addAll(it)
            foodResponsesFull.clear()
            foodResponsesFull.addAll(it)
            binding.rcFoodAdmin.adapter?.notifyDataSetChanged()
        })

        foodAdminViewModel.isLoading.observe(viewLifecycleOwner, Observer {
            binding.refresh.isRefreshing = it
        })

        foodAdminViewModel.errorResponse.observe(viewLifecycleOwner, Observer {
            if (it.error_code == 401) {
                Toast.makeText(requireContext(), "Đã hết phiên làm việc, vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show()
                (requireActivity() as MainActivity).openStart()
            } else {
                Toast.makeText(requireContext(),"Đã có lỗi xảy ra vui lòng thử lại sau", Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun getCount(): Int {
        return this.foodResponses.size
    }

    override fun getFoodAdmin(position: Int): FoodResponse {
        return this.foodResponses[position]
    }

    override fun onClickButtonItem(position: Int) {
        (requireActivity() as MainActivity).openFoodEditFragment(getFoodAdmin(position).id,
            getFoodAdmin(position).name, getFoodAdmin(position).image, getFoodAdmin(position).description, getFoodAdmin(position).price)
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
                binding.rcFoodAdmin.adapter?.notifyDataSetChanged()
            }
            R.id.ib_addFood -> {
                (requireActivity() as MainActivity).openFoodEditFragment(null, null, null, null, null)
            }
        }
    }

    override fun onBackPressForFragment() {
        (requireActivity() as MainActivity).openHomeFragment()
    }
}