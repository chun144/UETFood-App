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
import com.chun.uetfood.adapter.FeedbackAdapter
import com.chun.uetfood.adapter.FoodAdminAdapter
import com.chun.uetfood.common.SharedPreferenceCommon
import com.chun.uetfood.databinding.FragmentCommunicateBinding
import com.chun.uetfood.databinding.FragmentFeedbackBinding
import com.chun.uetfood.databinding.FragmentFoodAdminBinding
import com.chun.uetfood.model.response.FeedbackResponse
import com.chun.uetfood.model.response.FoodResponse
import com.chun.uetfood.model.response.QuestionResponse
import com.chun.uetfood.ui.base.fragment.BaseFragment
import com.chun.uetfood.ui.main.MainActivity
import com.chun.uetfood.viewmodel.CommunicateViewModel
import com.chun.uetfood.viewmodel.FeedbackViewModel

class FeedbackFragment: BaseFragment(), FeedbackAdapter.IFeedbackAdapter, View.OnClickListener {
    private lateinit var binding: FragmentFeedbackBinding
    private lateinit var feedbackViewModel : FeedbackViewModel
    private val feedbackResponses = mutableListOf<FeedbackResponse>()
    private lateinit var dialog : Dialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFeedbackBinding.inflate(inflater, container, false)
        feedbackViewModel = FeedbackViewModel()
        binding.data = feedbackViewModel
        binding.rcFeedback.adapter = FeedbackAdapter(this)
        binding.rcFeedback.layoutManager = LinearLayoutManager(requireContext())
        binding.ibBack.setOnClickListener(this)
        binding.ibAddFeedback.setOnClickListener(this)
        register()
        feedbackViewModel.getAllFeedback()
        initViews()
        return binding.root
    }

    private fun initViews() {
        binding.refresh.setOnRefreshListener {
            feedbackViewModel.getAllFeedback()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun register(){
        feedbackViewModel.feedbackDatas.observe(viewLifecycleOwner, Observer {
            feedbackResponses.clear()
            feedbackResponses.addAll(it.reversed())
            binding.rcFeedback.adapter?.notifyDataSetChanged()
        })

        feedbackViewModel.feedbackData.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), "Tạo phản hồi thành công", Toast.LENGTH_SHORT).show()
            feedbackViewModel.getAllFeedback()
            binding.rcFeedback.adapter?.notifyDataSetChanged()
        })

        feedbackViewModel.isLoading.observe(viewLifecycleOwner, Observer {
            binding.refresh.isRefreshing = it
        })

        feedbackViewModel.errorResponse.observe(viewLifecycleOwner, Observer {
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
        dialog.setContentView(R.layout.dialog_feedback)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        var buttonFeedback: AppCompatButton = dialog.findViewById(R.id.btn_sendFeedback)
        var buttonCancel: AppCompatButton = dialog.findViewById(R.id.btn_cancelFeedback)
        buttonFeedback.setOnClickListener(this)
        buttonCancel.setOnClickListener(this)
    }

    override fun getCount(): Int {
        return this.feedbackResponses.size
    }

    override fun getFeedback(position: Int): FeedbackResponse {
        return this.feedbackResponses[position]
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClick(view: View) {
        when (view.id) {
            R.id.ib_back -> {
                onBackPressForFragment()
            }
            R.id.ib_addFeedback -> {
                openDialog()
            }
            R.id.btn_sendFeedback -> {
                var edtFeedback: AppCompatEditText = dialog.findViewById(R.id.edt_feedback)
                var edtRate: AppCompatEditText = dialog.findViewById(R.id.edt_rate)
                var tvError: TextView = dialog.findViewById(R.id.tv_errorFeedback)
                if (edtFeedback.text.toString() == "") {
                    tvError.text = "Vui lòng nhập phản hồi"
                } else if (edtFeedback.text?.length!! > 1000) {
                    tvError.text = "Phản hồi không quá 1000 ký tự"
                } else if (edtRate.text.toString() == "" || edtRate.text.toString().toDouble() < 0.0 || edtRate.text.toString().toDouble() > 10.0) {
                    tvError.text = "Đánh giá từ 0 đến 10"
                } else {
                    dialog.cancel()
                    val user = SharedPreferenceCommon.readUser(requireContext())
                    feedbackViewModel.addFeedback(user.username, edtFeedback.text.toString(), edtRate.text.toString().toDouble())
                }
            }
            R.id.btn_cancelFeedback -> {
                dialog.cancel()
            }
        }
    }

    override fun onBackPressForFragment() {
        (requireActivity() as MainActivity).openHomeFragment()
    }
}