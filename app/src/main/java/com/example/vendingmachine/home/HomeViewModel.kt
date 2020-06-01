package com.example.vendingmachine.home

import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vendingmachine.network.APIService
import kotlinx.coroutines.*

class HomeViewModel : ViewModel(){


    var balance = 0

    val _balanceString = MutableLiveData<String>()

    val balanceString : LiveData<String>
        get() = _balanceString

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


    fun setApiKey(view : View){
        val key = view.contentDescription.toString()
        _apiKey.value = key
    }

    fun resetAPIKey(){
        _apiKey.value = null
    }

    fun resetDisplayedBalance(){
        balance = 0
        _balanceString.value = "000"
    }

}