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

    val _email_address = MutableLiveData<String>()
    val email_address : LiveData<String>
        get() = _email_address

    val _password = MutableLiveData<String>()
    val password : LiveData<String>
        get() = _password


    fun getUserStatus() {
        val user : FirebaseUser? = firebaseAuth.currentUser
        if (user != null) {
            val username = user.displayName
            _signInStatus.postValue(username)
        }else{
            _signInStatus.postValue(null)
        }
    }

    fun setUserName(username : String){
        _signInStatus.postValue(username)
    }

    fun setEmailAddress(inputEmail : String){
        _email_address.postValue(inputEmail)
    }

    fun setPassword(inputPassword : String){
        _password.postValue(inputPassword)
    }


    fun validateCredentials(userEmail : String, userPassword : String){
        if(userEmail != null && userPassword != null){
            firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnSuccessListener {
                it.user?.let { user ->
                    if (!user.displayName.isNullOrEmpty()){
                        setUserName(user.displayName.toString())
                    }else if(!user.email.isNullOrEmpty()){
                        setUserName(user.email.toString())
                    }
                }
            }
        }

    }

    init {
        getUserStatus()
    }


}