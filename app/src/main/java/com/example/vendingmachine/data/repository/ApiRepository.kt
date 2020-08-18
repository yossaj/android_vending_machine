package com.example.vendingmachine.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.vendingmachine.data.network.ApiService
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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

    fun testFireStore(){
        val db = Firebase.firestore
        coroutineScope.launch {
            db.collection("tasks")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Log.d("Firestore", "${document.id} => ${document.data}")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("Firestore", "Error getting documents.", exception)
                }
        }
    }

    init {
        testFireStore()
    }


}