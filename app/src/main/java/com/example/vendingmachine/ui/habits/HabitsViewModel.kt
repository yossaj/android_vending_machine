package com.example.vendingmachine.ui.habits

import androidx.fragment.app.viewModels
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.vendingmachine.data.models.Habit
import com.example.vendingmachine.data.repository.UserRepository
import com.example.vendingmachine.ui.tasks.TasksViewModel

class HabitsViewModel@ViewModelInject constructor(
    private val userRepository: UserRepository,
    @Assisted private val savedStateHandle: SavedStateHandle) : ViewModel(){


    val allHabits : LiveData<List<Habit>>
        get() = userRepository.allHabits

    val frequency : LiveData<Int>
        get() = userRepository.frequency

    fun addHabit(habit : Habit){
        userRepository.addHabit(habit)
    }

    fun listenForHabits(){
        userRepository.listenForHabitChanges()
    }

    fun incrementFrequency(){
        userRepository.incrementFrequency()
    }

    fun decrementFrequency(){
        userRepository.decrementFrequency()
    }

    fun updateHabitCount(habit: Habit, count : Int){
        userRepository.updateHabitCount(habit, count)
    }

    init {
        listenForHabits()
    }

    override fun onCleared() {
        userRepository.removeRegisteredHabitListener()
        super.onCleared()
    }




}