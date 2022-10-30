package com.chun.uetfood.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.chun.uetfood.R
import com.chun.uetfood.databinding.FragmentFoodEditBinding
import com.chun.uetfood.ui.base.fragment.BaseFragment
import com.chun.uetfood.ui.main.MainActivity
import com.chun.uetfood.viewmodel.FoodEditViewModel

class FoodEditFragment : BaseFragment(), View.OnClickListener {
    private lateinit var binding: FragmentFoodEditBinding
    private lateinit var viewModel: FoodEditViewModel
    private var foodId: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFoodEditBinding.inflate(inflater, container, false)
        viewModel = FoodEditViewModel()
        binding.data = viewModel
        initData()
        binding.btnAddFood.setOnClickListener(this)
        binding.btnEditFood.setOnClickListener(this)
        binding.btnDeleteFood.setOnClickListener(this)
        binding.ibBackFoodEdit.setOnClickListener(this)
        registerLiveData()
        return binding.root
    }

    private fun initData() {
        if (arguments != null) {
            val id = requireArguments().getInt("id")
            val name = requireArguments().getString("name")
            val image = requireArguments().getString("image")
            val description = requireArguments().getString("description")
            val price = requireArguments().getInt("price")

            foodId = id
            binding.tvTitle2.text = "Sửa món ăn"
            binding.edtName.setText(name)
            binding.edtImage.setText(image)
            binding.edtDescription.setText(description)
            binding.edtPrice.setText(price.toString())

            binding.btnEditFood.setBackgroundResource(R.drawable.bg_button_fix)
            binding.btnEditFood.text = "Sửa thông tin món ăn"
            binding.btnDeleteFood.setBackgroundResource(R.drawable.bg_button_logout)
            binding.btnDeleteFood.text = "Xóa món ăn"
            binding.btnAddFood.isEnabled = false
            binding.btnEditFood.isEnabled = true
            binding.btnDeleteFood.isEnabled = true

        } else {
            binding.tvTitle2.text = "Thêm món ăn"
            binding.btnAddFood.setBackgroundResource(R.drawable.bg_button)
            binding.btnAddFood.text = "Thêm món ăn mới"
            binding.btnAddFood.isEnabled = true
            binding.btnEditFood.isEnabled = false
            binding.btnDeleteFood.isEnabled = false
        }
    }

    private fun registerLiveData() {
        viewModel.foodData.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), "Thành công", Toast.LENGTH_SHORT).show()
            (requireActivity() as MainActivity).openFoodAdminFragment()
        })

        viewModel.deleteData.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), "Xóa thành công", Toast.LENGTH_SHORT).show()
            (requireActivity() as MainActivity).openFoodAdminFragment()
        })

        viewModel.errorResponse.observe(viewLifecycleOwner, Observer {
            if (it.error_code == 400 || it.error_code == 404) {
                binding.tvErrorFoodEdit.text = "Dữ liệu không hợp lệ, vui lòng thử lại sau"
            } else if (it.error_code == 401) {
                Toast.makeText(requireContext(),"Đã hết phiên làm việc, vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show()
                (requireActivity() as MainActivity).openStart()
            } else {
                binding.tvErrorFoodEdit.text = "Đã có lỗi xảy ra vui lòng thử lại sau"
            }
        })

    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.ib_backFoodEdit -> {
                onBackPressForFragment()
            }
            R.id.btn_addFood -> {
                if (binding.edtName.text.toString() != "" && binding.edtImage.text.toString() != ""
                    && binding.edtDescription.text.toString() != "" && binding.edtPrice.text.toString() != ""
                ) {
                    viewModel.addFood(
                        binding.edtName.text.toString(),
                        binding.edtImage.text.toString(),
                        binding.edtDescription.text.toString(),
                        Integer.parseInt(binding.edtPrice.text.toString()),
                    )
                } else {
                    binding.tvErrorFoodEdit.text = "Vui lòng nhập đầy đủ thông tin"
                }
            }
            R.id.btn_editFood -> {
                if (binding.edtName.text.toString() != "" && binding.edtImage.text.toString() != ""
                    && binding.edtDescription.text.toString() != "" && binding.edtPrice.text.toString() != ""
                ) {
                    viewModel.putFood(
                        foodId,
                        binding.edtName.text.toString(),
                        binding.edtImage.text.toString(),
                        binding.edtDescription.text.toString(),
                        Integer.parseInt(binding.edtPrice.text.toString()),
                    )
                } else {
                    binding.tvErrorFoodEdit.text = "Vui lòng nhập đầy đủ thông tin"
                }
            }
            R.id.btn_deleteFood -> {
                viewModel.deleteFood(foodId)
            }
        }
    }

    override fun onBackPressForFragment() {
        (requireActivity() as MainActivity).openFoodAdminFragment()
    }
}