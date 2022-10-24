package com.chun.uetfood.viewmodel

import android.annotation.SuppressLint
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chun.uetfood.model.request.LoginRequest
import com.chun.uetfood.model.response.ErrorResponse
import com.chun.uetfood.model.response.LoginResponse
import com.chun.uetfood.service.RetrofitFactor
import com.chun.uetfood.service.ServiceAppCallApi
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginViewModel : ViewModel {
    private val service: ServiceAppCallApi
    val loginData : MutableLiveData<LoginResponse>
    val errorResponse : MutableLiveData<ErrorResponse>
    val isLoading: ObservableBoolean

    constructor() {
        service = RetrofitFactor.createRetrofit()
        loginData = MutableLiveData()
        errorResponse = MutableLiveData()
        isLoading = ObservableBoolean()
    }

    @SuppressLint("CheckResult")
    fun login(username: String, password: String) {
        isLoading.set(true)
        service.login(
            LoginRequest(username, password)
        )
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    loginData.value = it
                    isLoading.set(false)
                },
                {
                    if (it is HttpException){
                        //lay data loi tra ve
                        val contentError = it.response().errorBody()?.string()
                        val error = Gson().fromJson(contentError, ErrorResponse::class.java)
                        error.error_code = it.code()
                        errorResponse.value = error
                    }
                    isLoading.set(false)
                })
    }
}