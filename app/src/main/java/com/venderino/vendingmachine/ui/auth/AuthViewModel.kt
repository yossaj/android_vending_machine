package com.venderino.vendingmachine.ui.auth

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthViewModel @ViewModelInject constructor(
    private val firebaseAuth: FirebaseAuth,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val _signInStatus = MutableLiveData<String?>()
    val signInStatus: LiveData<String?>
        get() = _signInStatus


    fun getUserStatus() {
        val user : FirebaseUser? = firebaseAuth.currentUser
        if (user != null) {
            if(!user.displayName.isNullOrEmpty()){
                val username = user.displayName
                _signInStatus.postValue(username)
            }else{
                val email = user.email
                _signInStatus.postValue(email)
            }

        }else{
            _signInStatus.postValue(null)
        }
    }

    fun setUserName(username : String){
        _signInStatus.postValue(username)
    }

    init {
        getUserStatus()
    }


}