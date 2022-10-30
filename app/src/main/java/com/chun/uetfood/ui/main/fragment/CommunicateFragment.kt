package com.chun.uetfood.ui.main.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chun.uetfood.R
import com.chun.uetfood.adapter.CommunicateAdapter
import com.chun.uetfood.adapter.FoodAdminAdapter
import com.chun.uetfood.common.SharedPreferenceCommon
import com.chun.uetfood.databinding.FragmentCommunicateBinding
import com.chun.uetfood.databinding.FragmentFoodAdminBinding
import com.chun.uetfood.model.response.FoodResponse
import com.chun.uetfood.model.response.QuestionResponse
import com.chun.uetfood.ui.base.fragment.BaseFragment
import com.chun.uetfood.ui.main.MainActivity
import com.chun.uetfood.viewmodel.CommunicateViewModel

class CommunicateFragment: BaseFragment(), CommunicateAdapter.ICommunicateAdapter, View.OnClickListener {
    private lateinit var binding: FragmentCommunicateBinding
    private lateinit var communicateViewModel : CommunicateViewModel
    private val questionResponses = mutableListOf<QuestionResponse>()
    private lateinit var dialog : Dialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommunicateBinding.inflate(inflater, container, false)
        communicateViewModel = CommunicateViewModel()
        binding.data = communicateViewModel
        binding.rcQuestion.adapter = CommunicateAdapter(this)
        binding.rcQuestion.layoutManager = LinearLayoutManager(requireContext())
        binding.ibBack.setOnClickListener(this)
        binding.ibAddQuestion.setOnClickListener(this)
        register()
        initData()
        initViews()
        return binding.root
    }

    private fun initData() {
        val user = SharedPreferenceCommon.readUser(requireContext())
        when (user.role) {
            "admin" -> {
                communicateViewModel.getAllQuestion()
            }
            "user" -> {
                binding.ibAddQuestion.setOnClickListener(this)
                binding.ibAddQuestion.setImageResource(R.drawable.baseline_add_circle_blue_800_36dp)
                communicateViewModel.getQuestionUser(user.username)
            }
        }
    }

    private fun initViews() {
        binding.refresh.setOnRefreshListener {
            val user = SharedPreferenceCommon.readUser(requireContext())
            when (user.role) {
                "admin" -> {
                    communicateViewModel.getAllQuestion()
                }
                "user" -> {
                    communicateViewModel.getQuestionUser(user.username)
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun register(){
        communicateViewModel.questionDatas.observe(viewLifecycleOwner, Observer {
            questionResponses.clear()
            questionResponses.addAll(it.reversed())
            binding.rcQuestion.adapter?.notifyDataSetChanged()
        })

        communicateViewModel.questionData.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), "Tạo câu hỏi thành công", Toast.LENGTH_SHORT).show()
            val user = SharedPreferenceCommon.readUser(requireContext())
            communicateViewModel.getQuestionUser(user.username)
            binding.rcQuestion.adapter?.notifyDataSetChanged()
        })

        communicateViewModel.isLoading.observe(viewLifecycleOwner, Observer {
            binding.refresh.isRefreshing = it
        })

        communicateViewModel.errorResponse.observe(viewLifecycleOwner, Observer {
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

    private fun openDialog() {
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_question)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        var buttonQuestion: AppCompatButton = dialog.findViewById(R.id.btn_sendQuestion)
        var buttonCancel: AppCompatButton = dialog.findViewById(R.id.btn_cancelQuestion)
        buttonQuestion.setOnClickListener(this)
        buttonCancel.setOnClickListener(this)
    }

    override fun getCount(): Int {
        return this.questionResponses.size
    }

    override fun getQuestion(position: Int): QuestionResponse {
        return this.questionResponses[position]
    }

    override fun onClickItem(position: Int) {
        (requireActivity() as MainActivity).openQuestionFragment(getQuestion(position).id)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClick(view: View) {
        when (view.id) {
            R.id.ib_back -> {
                onBackPressForFragment()
            }
            R.id.ib_addQuestion -> {
                openDialog()
            }
            R.id.btn_sendQuestion-> {
                var edtQuestion: AppCompatEditText = dialog.findViewById(R.id.edt_question)
                var tvError: TextView = dialog.findViewById(R.id.tv_errorQuestion)
                if (edtQuestion.text.toString() == "") {
                    tvError.text = "Vui lòng nhập câu hỏi"
                } else if (edtQuestion.text?.length!! > 1000) {
                    tvError.text = "Câu hỏi không quá 1000 ký tự"
                } else {
                    dialog.cancel()
                    val user = SharedPreferenceCommon.readUser(requireContext())
                    communicateViewModel.addQuestion(user.username, edtQuestion.text.toString())
                }
            }
            R.id.btn_cancelQuestion -> {
                dialog.cancel()
            }
        }
    }

    override fun onBackPressForFragment() {
        (requireActivity() as MainActivity).openHomeFragment()
    }
}