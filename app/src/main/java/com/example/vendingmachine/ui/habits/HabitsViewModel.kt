package com.example.vendingmachine.ui.habits

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.vendingmachine.data.models.Habit
import com.example.vendingmachine.data.repository.UserRepository
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
                if(it.count != 0 && it.frequency == 1 && dailyHabitIsDue(it.updatedAt)){
                        it.count = 0
                        updateHabit(it)
                }else if(it.count != 0 && it.frequency == 2 && weeklyHabitIsDue(it.updatedAt)){
                        it.count = 0
                        updateHabit(it)
                }else if(it.count != 0 && it.frequency == 3 && monthlyHabitIsDue(it.updatedAt)){
                    it.count = 0
                    updateHabit(it)
                }
            }
        }
    }

    fun dailyHabitIsDue(updated : Long) : Boolean{
        val habitLastUpdated: Calendar = Calendar.getInstance()
        val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        habitLastUpdated.setTimeInMillis(updated)
        val lastUpdatedDay = habitLastUpdated.get(Calendar.DAY_OF_YEAR)
        return lastUpdatedDay != currentDay
    }

    fun weeklyHabitIsDue(updated : Long) : Boolean{
        val habitLastUpdated: Calendar = Calendar.getInstance()
        val currentWeek = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)
        habitLastUpdated.setTimeInMillis(updated)
        val lastUpdatedWeek = habitLastUpdated.get(Calendar.WEEK_OF_YEAR)
        return lastUpdatedWeek != currentWeek
    }

    fun monthlyHabitIsDue(updated: Long) : Boolean{
        val habitLastUpdated: Calendar = Calendar.getInstance()
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        habitLastUpdated.setTimeInMillis(updated)
        val lastUpdatedMonth = habitLastUpdated.get(Calendar.MONTH)
        return lastUpdatedMonth != currentMonth
    }

    override fun onCleared() {
        userRepository.removeRegisteredHabitListener()
        super.onCleared()
    }




}