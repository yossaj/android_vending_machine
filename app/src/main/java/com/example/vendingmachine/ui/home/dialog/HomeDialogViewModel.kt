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

    fun getVendedApi(context : Context) {
        val url = UrlParser.selectAPI(apiKey)
        coroutineScope.launch {
            val data = RetrofitBuilder.buildServiceFor(url)
            withContext(Dispatchers.Main) {
                when(apiKey){
                    "Cat" ->
                        _responseString.value = data.getCatPic()[0].url
                    "Dog" ->
                        _responseString.value = data.getDogPic().message
                    "Mustache" ->
                        _responseString.value = data.getSwansonWisdom()[0]
                    "Advice" ->
                        _responseString.value = data.getAdvice().slip.advice
                    "Bull" ->
                        _responseString.value = data.getBull().phrase
                }

            }
        }
    }



}