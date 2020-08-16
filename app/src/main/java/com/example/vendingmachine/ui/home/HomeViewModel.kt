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

    val zeroFunds = "000"

    val _numberOfCoins = MutableLiveData<Int>()

    val numberOfCoins : LiveData<Int>
        get() = _numberOfCoins

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


    fun setApiKey(view : View){
        resetAPIKey()
        val key = view.contentDescription.toString()
        _apiKey.value = key
    }

    fun resetAPIKey(){
        _apiKey.value = null
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
        when (apiKey.value) {
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


    init {
        _balanceString.value = zeroFunds
    }

}