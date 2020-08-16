package com.example.vendingmachine.ui.home.dialog

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vendingmachine.data.network.ApiService
import com.example.vendingmachine.data.network.RetrofitBuilder
import com.example.vendingmachine.utils.UrlParser
import kotlinx.coroutines.*

class HomeDialogViewModel(val apiKey: String) : ViewModel(){

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.IO)

    val _responseString = MutableLiveData<String?>()

    val responseString : LiveData<String?>
        get() = _responseString

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