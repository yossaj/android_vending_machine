package com.example.vendingmachine.data.repository

import androidx.lifecycle.MutableLiveData
import com.example.vendingmachine.data.network.ApiService
import kotlinx.coroutines.*

class ApiRepository constructor(private val apiService: ApiService){

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.IO)

    val _responseString = MutableLiveData<String?>()

    fun resetResponseString(){
        _responseString.postValue(null)
    }

    val _apiKey = MutableLiveData<String>()

    fun setApi(key : String){
        _apiKey.postValue(key)
    }

    fun resetApiKey(){
        _apiKey.postValue(null)
    }

    fun getVendedApi() {
        coroutineScope.launch {
            val data = filterAndMakeRequest()
            withContext(Dispatchers.Main){
                _responseString.postValue(data)
            }
        }
    }

    private suspend fun filterAndMakeRequest() : String {
        when (_apiKey.value) {
            "Cat" ->
                return apiService.getCatPic()[0].url
            "Dog" ->
                return apiService.getDogPic().message
            "Mustache" ->
                return apiService.getSwansonWisdom()[0]
            "Advice" ->
                return  apiService.getAdvice().slip.advice
            "Bull" ->
                return apiService.getBull().phrase
            else ->
                return "ERROR : Unable to retrieve data"
        }
    }

}