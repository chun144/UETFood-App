package com.chun.uetfood.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chun.uetfood.common.CommonApp
import com.chun.uetfood.common.SharedPreferenceCommon
import com.chun.uetfood.model.request.CartRequest
import com.chun.uetfood.model.response.ErrorResponse
import com.chun.uetfood.model.response.FoodResponse
import com.chun.uetfood.model.response.MessageResponse
import com.chun.uetfood.service.RetrofitFactor
import com.chun.uetfood.service.ServiceAppCallApi
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class CartViewModel : ViewModel {
    private val service: ServiceAppCallApi
    val errorResponse : MutableLiveData<ErrorResponse>
    val foodData : MutableLiveData<MutableList<FoodResponse>>
    val isLoading: MutableLiveData<Boolean>
    val deleteData : MutableLiveData<MessageResponse>

    constructor() {
        service = RetrofitFactor.createRetrofitToken(
            SharedPreferenceCommon.readUserToken(CommonApp.getContextApp())
        )
        errorResponse = MutableLiveData()
        foodData = MutableLiveData()
        deleteData = MutableLiveData()
        isLoading = MutableLiveData()
    }

    @SuppressLint("CheckResult")
    fun getFoodCart(username: String) {
        isLoading.value = true

        service.getFoodCart(username)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    foodData.value = it
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
    fun deleteCart(username: String, foodId: Int) {
        isLoading.value = true

        service.deleteCart(CartRequest(username, foodId))
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    deleteData.value = it
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