package com.example.vendingmachine.ui.habits

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.vendingmachine.data.models.Habit
import com.example.vendingmachine.data.repository.UserRepository
import java.text.SimpleDateFormat
import java.util.*

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

    fun updateHabit(habit: Habit){
        userRepository.updateHabit(habit)
    }

    fun resetDailyHabits(){
        allHabits.value?.let {
            it.forEach {
                val dateCheck = habitIsDue(it.updatedAt)
                if(!dateCheck && it.count != 0 && it.frequency == 1){
                    it.count = 0
                    updateHabit(it)
                }
            }
        }
    }

    fun habitIsDue(updated : Long) : Boolean{
        val habitLastUpdated: Calendar = Calendar.getInstance()
        val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        habitLastUpdated.setTimeInMillis(updated)
        val lastUpdatedDay = habitLastUpdated.get(Calendar.DAY_OF_YEAR)
        return lastUpdatedDay == currentDay
    }

    init {
        listenForHabits()
    }

    override fun onCleared() {
        userRepository.removeRegisteredHabitListener()
        super.onCleared()
    }




}