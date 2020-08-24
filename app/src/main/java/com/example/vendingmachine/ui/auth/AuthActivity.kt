package com.example.vendingmachine.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.vendingmachine.databinding.ActivityAuthBinding
import com.firebase.ui.auth.AuthUI
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AuthActivity : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

//        val SIGN_IN_CODE = 1010
//        val providers = arrayListOf(
//            AuthUI.IdpConfig.EmailBuilder().build()
//        )
//
//
//
//        startActivityForResult(
//            AuthUI.getInstance()
//                .createSignInIntentBuilder()
//                .setAvailableProviders(providers)
//                .build(),
//            SIGN_IN_CODE
//        )

    }


}