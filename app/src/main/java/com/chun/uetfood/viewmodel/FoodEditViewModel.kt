package com.chun.uetfood.viewmodel

import android.annotation.SuppressLint
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chun.uetfood.common.CommonApp
import com.chun.uetfood.common.SharedPreferenceCommon
import com.chun.uetfood.model.request.FoodRequest
import com.chun.uetfood.model.response.ErrorResponse
import com.chun.uetfood.model.response.FoodResponse
import com.chun.uetfood.model.response.MessageResponse
import com.chun.uetfood.service.RetrofitFactor
import com.chun.uetfood.service.ServiceAppCallApi
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class FoodEditViewModel : ViewModel {
    private val service: ServiceAppCallApi
    val foodData : MutableLiveData<FoodResponse>
    val deleteData : MutableLiveData<MessageResponse>
    val errorResponse : MutableLiveData<ErrorResponse>
    val isLoading: ObservableBoolean

    constructor() {
        service = RetrofitFactor.createRetrofitToken(
            SharedPreferenceCommon.readUserToken(CommonApp.getContextApp())
        )
        foodData = MutableLiveData()
        deleteData = MutableLiveData()
        errorResponse = MutableLiveData()
        isLoading = ObservableBoolean()
    }

    @SuppressLint("CheckResult")
    fun addFood(name: String, image: String, description: String, price: Int) {
        isLoading.set(true)
        service.addFood(
            FoodRequest(name, image, description, price)
        )
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    foodData.value = it
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
    fun putFood(id: Int, name: String, image: String, description: String, price: Int) {
        isLoading.set(true)
        service.putFood(id, FoodRequest(name, image, description, price))
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    foodData.value = it
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
    fun deleteFood(id: Int) {
        isLoading.set(true)
        service.deleteFood(id)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    deleteData.value = it
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