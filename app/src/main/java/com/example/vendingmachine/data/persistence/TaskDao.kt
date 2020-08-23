package com.example.vendingmachine.data.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.vendingmachine.data.models.Habit
import com.example.vendingmachine.data.models.Task

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks")
    fun getTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM tasks WHERE entryid = :taskId")
    fun getTaskById(taskId: String): Task
//
    @Query("SELECT title FROM tasks WHERE complete = 0")
    fun getIncompleteTasksTitles() : List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(task: Task)

    @Update
    fun updateTask(task: Task): Int

    @Query("DELETE FROM tasks WHERE entryid = :taskId")
    fun deleteTaskById(taskId: String): Int

    @Query("DELETE FROM tasks")
    fun deleteAllTasks()

    @Query("DELETE FROM tasks WHERE complete = 1")
    fun deleteCompletedTasks(): Int


}