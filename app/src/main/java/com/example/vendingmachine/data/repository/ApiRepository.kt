package com.example.vendingmachine.data.repository

import androidx.lifecycle.MutableLiveData
import com.example.vendingmachine.data.network.ApiService
import com.example.vendingmachine.data.network.RetrofitBuilder
import kotlinx.coroutines.*

class ApiRepository constructor(private val apiService: ApiService){

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.IO)

    val _responseString = MutableLiveData<String?>()


    val apiKey = "Cat"
    fun getVendedApi() {
        coroutineScope.launch {
            val data = filterAndMakeRequest()
            withContext(Dispatchers.Main){
                _responseString.postValue(data)
            }
        }
    }

    private suspend fun filterAndMakeRequest() : String {
        when (apiKey) {
            "Cat" ->
                return RetrofitBuilder.buildServiceFor().getCatPic()[0].url
            "Dog" ->
                return RetrofitBuilder.buildServiceFor().getDogPic().message
            "Mustache" ->
                return RetrofitBuilder.buildServiceFor().getSwansonWisdom()[0]
            "Advice" ->
                return  RetrofitBuilder.buildServiceFor().getAdvice().slip.advice
            "Bull" ->
                return RetrofitBuilder.buildServiceFor().getBull().phrase
            else ->
                return "ERROR : Unable to retrieve data"
        }
    }

}