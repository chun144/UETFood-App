package com.chun.uetfood.viewmodel

import android.annotation.SuppressLint
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chun.uetfood.common.CommonApp
import com.chun.uetfood.common.SharedPreferenceCommon
import com.chun.uetfood.model.request.AnswerRequest
import com.chun.uetfood.model.request.QuestionRequest
import com.chun.uetfood.model.response.AnswerResponse
import com.chun.uetfood.model.response.ErrorResponse
import com.chun.uetfood.model.response.QuestionResponse
import com.chun.uetfood.service.RetrofitFactor
import com.chun.uetfood.service.ServiceAppCallApi
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class QuestionViewModel : ViewModel {
    private val service: ServiceAppCallApi
    val errorResponse : MutableLiveData<ErrorResponse>
    val questionData : MutableLiveData<QuestionResponse>
    val answerData : MutableLiveData<AnswerResponse>
    val isLoading: ObservableBoolean

    constructor() {
        service = RetrofitFactor.createRetrofitToken(
            SharedPreferenceCommon.readUserToken(CommonApp.getContextApp())
        )
        errorResponse = MutableLiveData()
        questionData = MutableLiveData()
        answerData = MutableLiveData()
        isLoading = ObservableBoolean()
    }

    @SuppressLint("CheckResult")
    fun getQuestion(questionId: Int) {
        isLoading.set(true)

        service.getQuestion(questionId)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    questionData.value = it
                    isLoading.set(false)
                },
                {
                    if (it is HttpException){
                        val error = ErrorResponse()
                        error.error_code = it.code()
                        error.error_message = it.message()
                        errorResponse.value = error
                    }
                    isLoading.set(false)
                })
    }

    @SuppressLint("CheckResult")
    fun addAnswer(username: String, text: String, questionId: Int) {
        isLoading.set(true)

        service.addAnswer(
            AnswerRequest(username, text, questionId)
        )
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    answerData.value = it
                    isLoading.set(false)
                },
                {
                    if (it is HttpException){
                        val error = ErrorResponse()
                        error.error_code = it.code()
                        error.error_message = it.message()
                        errorResponse.value = error
                    }
                    isLoading.set(false)
                })
    }
}