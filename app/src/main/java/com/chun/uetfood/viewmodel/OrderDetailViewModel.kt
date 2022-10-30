package com.chun.uetfood.viewmodel

import android.annotation.SuppressLint
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chun.uetfood.common.CommonApp
import com.chun.uetfood.common.SharedPreferenceCommon
import com.chun.uetfood.model.response.ErrorResponse
import com.chun.uetfood.model.response.OrderResponse
import com.chun.uetfood.service.RetrofitFactor
import com.chun.uetfood.service.ServiceAppCallApi
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class OrderDetailViewModel : ViewModel {
    private val service: ServiceAppCallApi
    val errorResponse : MutableLiveData<ErrorResponse>
    val orderData : MutableLiveData<OrderResponse>
    val isLoading: ObservableBoolean

    constructor() {
        service = RetrofitFactor.createRetrofitToken(
            SharedPreferenceCommon.readUserToken(CommonApp.getContextApp())
        )
        errorResponse = MutableLiveData()
        orderData = MutableLiveData()
        isLoading = ObservableBoolean()
    }

    @SuppressLint("CheckResult")
    fun getOrder(id: Int) {
        isLoading.set(true)

        service.getOrder(id)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    orderData.value = it
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