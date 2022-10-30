package com.chun.uetfood.viewmodel

import android.annotation.SuppressLint
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chun.uetfood.model.request.RegisterRequest
import com.chun.uetfood.model.response.ErrorResponse
import com.chun.uetfood.model.response.MessageResponse
import com.chun.uetfood.service.RetrofitFactor
import com.chun.uetfood.service.ServiceAppCallApi
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class RegisterViewModel : ViewModel {
    private val service: ServiceAppCallApi
    val errorResponse : MutableLiveData<ErrorResponse>
    val registerData : MutableLiveData<MessageResponse>
    val isLoading: ObservableBoolean

    constructor() {
        service = RetrofitFactor.createRetrofit()
        errorResponse = MutableLiveData()
        registerData = MutableLiveData()
        isLoading = ObservableBoolean()
    }

    @SuppressLint("CheckResult")
    fun register(username: String, password: String, password2: String, nickname: String, role: String) {
        isLoading.set(true)

        service.register(
            RegisterRequest(username, password, password2, nickname, role)
        )
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    registerData.value = it
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