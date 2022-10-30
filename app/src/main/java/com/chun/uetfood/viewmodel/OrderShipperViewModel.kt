package com.chun.uetfood.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chun.uetfood.common.CommonApp
import com.chun.uetfood.common.SharedPreferenceCommon
import com.chun.uetfood.model.request.OrderShipperRequest
import com.chun.uetfood.model.response.ErrorResponse
import com.chun.uetfood.model.response.OrderResponse
import com.chun.uetfood.model.response.OrderShipperResponse
import com.chun.uetfood.service.RetrofitFactor
import com.chun.uetfood.service.ServiceAppCallApi
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class OrderShipperViewModel : ViewModel {
    private val service: ServiceAppCallApi
    val errorResponse : MutableLiveData<ErrorResponse>
    val orderData : MutableLiveData<MutableList<OrderResponse>>
    val orderShipperData : MutableLiveData<OrderShipperResponse>
    val isLoading: MutableLiveData<Boolean>

    constructor() {
        service = RetrofitFactor.createRetrofitToken(
            SharedPreferenceCommon.readUserToken(CommonApp.getContextApp())
        )
        errorResponse = MutableLiveData()
        orderData = MutableLiveData()
        orderShipperData = MutableLiveData()
        isLoading = MutableLiveData()
    }

    @SuppressLint("CheckResult")
    fun getAllOrder() {
        isLoading.value = true

        service.getAllOrder()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    orderData.value = it
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
    fun addOrderShipper(username: String, orderId: Int) {
        isLoading.value = true

        service.addOrderShipper(
            OrderShipperRequest(username, orderId)
        )
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    orderShipperData.value = it
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