package com.example.vendingmachine.home

import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vendingmachine.network.APIService
import kotlinx.coroutines.*

class HomeViewModel : ViewModel(){
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.IO)

    var balance = 0

    val _balanceString = MutableLiveData<String>()

    val balanceString : LiveData<String>
        get() = _balanceString

    val _responseString = MutableLiveData<String>()

    val responseString : LiveData<String>
        get() = _responseString

    val _apiKey = MutableLiveData<String>()

    val apiKey : LiveData<String>
        get() = _apiKey


    fun addToBalance(){
        balance += 100
    }

    fun addToBalanceAndConvertToString(){
        addToBalance()
        _balanceString.value = balance.toString()
    }

    init {
        _balanceString.value = "000"
    }


    fun getVendedApi(context : Context) {
        coroutineScope.launch {
            val response = APIService.vend(apiKey.value)

            withContext(Dispatchers.Main) {
                _responseString.value = response
            }
        }
    }

    fun setApiKey(view : View){
        val key = view.contentDescription.toString()
        _apiKey.value = key
    }

}