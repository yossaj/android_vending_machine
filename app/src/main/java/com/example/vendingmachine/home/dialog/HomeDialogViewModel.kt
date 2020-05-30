package com.example.vendingmachine.home.dialog

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vendingmachine.network.APIService
import kotlinx.coroutines.*

class HomeDialogViewModel : ViewModel(){

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.IO)

    val _responseString = MutableLiveData<String>()

    val responseString : LiveData<String>
        get() = _responseString

    val _apiKey = MutableLiveData<String>()

    val apiKey : LiveData<String>
        get() = _apiKey


    fun getVendedApi(context : Context) {
        coroutineScope.launch {
            val response = APIService.vend(apiKey.value)

            withContext(Dispatchers.Main) {
                _responseString.value = response
            }
        }
    }

}