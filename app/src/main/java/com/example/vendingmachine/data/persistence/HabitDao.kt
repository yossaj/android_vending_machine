package com.example.vendingmachine.data.persistence

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.vendingmachine.data.models.Habit

@Dao
interface HabitDao {

    @Query("SELECT * FROM habits")
    fun getHabits(): LiveData<List<Habit>>

    @Query("SELECT * FROM habits WHERE id = :habitId ")
    fun getHabitById(habitId : String) : LiveData<Habit>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHabit(habit: Habit)

    @Query("DELETE FROM habits WHERE id = :habitId")
    fun deleteHabitById(habitId: String): Int

    @Query("DELETE FROM habits")
    fun deleteAllHabits()
}