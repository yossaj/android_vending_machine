package com.example.vendingmachine.ui.habits

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.vendingmachine.data.models.Habit
import com.example.vendingmachine.data.repository.UserRepository

class HabitsViewModel@ViewModelInject constructor(
    private val userRepository: UserRepository,
    @Assisted private val savedStateHandle: SavedStateHandle) : ViewModel(){

    fun addHabit(habit : Habit){
        userRepository.addHabit(habit)
    }


}