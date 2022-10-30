package com.chun.uetfood.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chun.uetfood.common.CommonApp
import com.chun.uetfood.common.SharedPreferenceCommon
import com.chun.uetfood.model.request.QuestionRequest
import com.chun.uetfood.model.response.ErrorResponse
import com.chun.uetfood.model.response.QuestionResponse
import com.chun.uetfood.service.RetrofitFactor
import com.chun.uetfood.service.ServiceAppCallApi
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class CommunicateViewModel : ViewModel {
    private val service: ServiceAppCallApi
    val errorResponse : MutableLiveData<ErrorResponse>
    val questionDatas : MutableLiveData<MutableList<QuestionResponse>>
    val questionData : MutableLiveData<QuestionResponse>
    val isLoading: MutableLiveData<Boolean>

    constructor() {
        service = RetrofitFactor.createRetrofitToken(
            SharedPreferenceCommon.readUserToken(CommonApp.getContextApp())
        )
        errorResponse = MutableLiveData()
        questionDatas = MutableLiveData()
        questionData = MutableLiveData()
        isLoading = MutableLiveData()
    }

    @SuppressLint("CheckResult")
    fun getAllQuestion() {
        isLoading.value = true

        service.getAllQuestion()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    questionDatas.value = it
                    isLoading.value = false
                },
                {
                    if (it is HttpException){
                        val error = ErrorResponse()
                        error.error_code = it.code()
                        error.error_message = it.message()
                        errorResponse.value = error
                    }
                    isLoading.value = false
                })
    }

    @SuppressLint("CheckResult")
    fun getQuestionUser(username: String) {
        isLoading.value = true

        service.getQuestionUser(username)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    questionDatas.value = it
                    isLoading.value = false
                },
                {
                    if (it is HttpException){
                        val error = ErrorResponse()
                        error.error_code = it.code()
                        error.error_message = it.message()
                        errorResponse.value = error
                    }
                    isLoading.value = false
                })
    }

    @SuppressLint("CheckResult")
    fun addQuestion(username: String, text: String) {
        isLoading.value = true

        service.addQuestion(
            QuestionRequest(username, text)
        )
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    questionData.value = it
                    isLoading.value = false
                },
                {
                    if (it is HttpException){
                        val error = ErrorResponse()
                        error.error_code = it.code()
                        error.error_message = it.message()
                        errorResponse.value = error
                    }
                    isLoading.value = false
                })
    }
}