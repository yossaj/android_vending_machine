package com.venderino.vendingmachine.ui.settings

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SettingsViewModel@ViewModelInject constructor(
    private val firebaseAuth: FirebaseAuth,
    @Assisted private val savedStateHandle: SavedStateHandle
)  : ViewModel(){

    val _currentUser = MutableLiveData<FirebaseUser?>()
    val currentUser : LiveData<FirebaseUser?>
        get() = _currentUser


    fun signOut(){
        firebaseAuth.signOut()
    }

    fun getUser(){
        _currentUser.postValue(firebaseAuth.currentUser)
    }

    init {
        getUser()
    }

}