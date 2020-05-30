package com.example.vendingmachine.home.dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class HomeDialogViewModelFactory(val apiKey : String) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeDialogViewModel::class.java)) {
                return HomeDialogViewModel() as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
}