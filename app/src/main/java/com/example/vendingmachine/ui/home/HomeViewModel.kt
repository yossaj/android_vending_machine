package com.example.vendingmachine.ui.home

import android.view.View
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.vendingmachine.data.network.RetrofitBuilder
import com.example.vendingmachine.data.repository.ApiRepository
import kotlinx.coroutines.*

class HomeViewModel@ViewModelInject constructor(
    private val apiRepository: ApiRepository,
    @Assisted private val savedStateHandle: SavedStateHandle) : ViewModel(){

    var balance = 0

    val _clicked = MutableLiveData<Boolean>()
    val clicked : LiveData<Boolean>
        get() = _clicked

    val zeroFunds = "000"

    val _numberOfCoins = MutableLiveData<Int>()

    val numberOfCoins : LiveData<Int>
        get() = _numberOfCoins

    val _balanceString = MutableLiveData<String>()

    val balanceString : LiveData<String>
        get() = _balanceString

    val apiKey : LiveData<String>
        get() = apiRepository._apiKey

    fun setApiKey(key : String){
        apiRepository.setApi(key)
    }

    fun addToBalance(){
        balance += 100
    }

    fun addToBalanceAndConvertToString(){
        addToBalance()
        _balanceString.value = balance.toString()
    }

    fun setApiKey(view : View){
        resetAPIKey()
        val key = view.contentDescription.toString()
        setApiKey(key)
        _clicked.postValue(true)
    }

    fun resetClicked(){
        _clicked.postValue(false)
    }

    fun resetAPIKey(){
        apiRepository.resetApiKey()
    }

    fun updateDisplayedBalanceUponSale(){
        if(balance == 100) {
            balance = 0
            _balanceString.value = zeroFunds
        }else {
            balance -= 100
            _balanceString.value = balance.toString()
        }
    }

    fun reduceCoinCountByOne(){
        _numberOfCoins.value?.let {
            if(it > 0) {
                _numberOfCoins.value = _numberOfCoins.value?.minus(1)
            }
        }
    }

    val responseString : LiveData<String?>
        get() = apiRepository._responseString

    fun resetResponse(){
        apiRepository.resetResponseString()
    }

    fun getVendedApi() {
        apiRepository.getVendedApi()
    }


    init {
        _balanceString.value = zeroFunds
    }

}