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
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chun.uetfood.R
import com.chun.uetfood.adapter.AnswerAdapter
import com.chun.uetfood.common.SharedPreferenceCommon
import com.chun.uetfood.databinding.FragmentQuestionBinding
import com.chun.uetfood.model.response.AnswerResponse
import com.chun.uetfood.model.response.QuestionResponse
import com.chun.uetfood.ui.base.fragment.BaseFragment
import com.chun.uetfood.ui.main.MainActivity
import com.chun.uetfood.viewmodel.QuestionViewModel

class QuestionFragment: BaseFragment(), AnswerAdapter.IAnswerAdapter, View.OnClickListener {
    private lateinit var binding: FragmentQuestionBinding
    private lateinit var questionViewModel : QuestionViewModel
    private var questionResponse = QuestionResponse()
    private lateinit var dialog : Dialog
    private var questionId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuestionBinding.inflate(inflater, container, false)
        questionViewModel = QuestionViewModel()
        binding.data2 = questionViewModel
        binding.rcAnswer.adapter = AnswerAdapter(this)
        binding.rcAnswer.layoutManager = LinearLayoutManager(requireContext())
        binding.ibBack.setOnClickListener(this)
        binding.ibAddAnswer.setOnClickListener(this)
        initData()
        register()
        questionViewModel.getQuestion(questionId)
        return binding.root
    }

    private fun initData() {
        if (arguments != null) {
            questionId = requireArguments().getInt("questionId")
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun register(){
        questionViewModel.questionData.observe(viewLifecycleOwner, Observer {
            questionResponse = it
            binding.data = questionResponse
            binding.rcAnswer.adapter?.notifyDataSetChanged()
        })

        questionViewModel.answerData.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), "Tạo câu trả lời thành công", Toast.LENGTH_SHORT).show()
            questionViewModel.getQuestion(questionId)
            binding.rcAnswer.adapter?.notifyDataSetChanged()
        })

        questionViewModel.errorResponse.observe(viewLifecycleOwner, Observer {
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
        dialog.setContentView(R.layout.dialog_answer)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        var buttonAnswer: AppCompatButton = dialog.findViewById(R.id.btn_sendAnswer)
        var buttonCancel: AppCompatButton = dialog.findViewById(R.id.btn_cancelAnswer)
        buttonAnswer.setOnClickListener(this)
        buttonCancel.setOnClickListener(this)
    }

    override fun getCount(): Int {
        return this.questionResponse.listAnswer.size
    }

    override fun getAnswer(position: Int): AnswerResponse {
        return this.questionResponse.listAnswer[position]
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClick(view: View) {
        when (view.id) {
            R.id.ib_back -> {
                onBackPressForFragment()
            }
            R.id.ib_addAnswer -> {
                openDialog()
            }
            R.id.btn_sendAnswer-> {
                var edtAnswer: AppCompatEditText = dialog.findViewById(R.id.edt_answer)
                var tvError: TextView = dialog.findViewById(R.id.tv_errorAnswer)
                if (edtAnswer.text.toString() == "") {
                    tvError.text = "Vui lòng nhập câu trả lời"
                } else if (edtAnswer.text?.length!! > 1000) {
                    tvError.text = "Câu trả lời không quá 1000 ký tự"
                } else {
                    dialog.cancel()
                    val user = SharedPreferenceCommon.readUser(requireContext())
                    questionViewModel.addAnswer(user.username, edtAnswer.text.toString(), questionId)
                }
            }
            R.id.btn_cancelAnswer -> {
                dialog.cancel()
            }
        }
    }

    override fun onBackPressForFragment() {
        (requireActivity() as MainActivity).openCommunicateFragment()
    }
}