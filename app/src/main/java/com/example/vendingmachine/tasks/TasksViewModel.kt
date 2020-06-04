package com.example.vendingmachine.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TasksViewModel : ViewModel(){

    val _coinIncrementSwitch = MutableLiveData<Boolean>()

    val coinIncrementSwitch : LiveData<Boolean>
        get() = _coinIncrementSwitch


    fun incrementCoinSwitch(){
        _coinIncrementSwitch.value = true
    }

    init {
        _coinIncrementSwitch.value = false
    }

}