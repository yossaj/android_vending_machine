package com.example.vendingmachine.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel(){

    var balance = 0

    val _balanceString = MutableLiveData<String>()

    val balanceString : LiveData<String>
    get() = _balanceString




    fun addToBalance(){
        balance += 100
        _balanceString.value = balance.toString()
    }

    init {
        _balanceString.value = "000"
    }

}