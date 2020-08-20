package com.example.vendingmachine.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vendingmachine.data.models.Task
import com.example.vendingmachine.data.persistence.HabitDao
import com.example.vendingmachine.data.persistence.TaskDao
import kotlinx.coroutines.*

class UserRepository constructor(private val taskDao: TaskDao, private val habitDao: HabitDao){

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var allTasks = taskDao.getTasks()

    val _currentTask = MutableLiveData<Task>()
    val currentNewTask : LiveData<Task>
        get() = _currentTask

    fun updateOnComplete(task: Task){
        uiScope.launch {
            withContext(Dispatchers.IO) {
                taskDao.updateTask(task)
            }
        }
    }

    fun deleteTask(){
        uiScope.launch {
            _currentTask.value?.id?.let {
                withContext(Dispatchers.IO) {
                    taskDao.deleteTaskById(it)
                }
            }
        }
    }

    fun addTask(){
        uiScope.launch {
            currentNewTask.value?.let {
                withContext(Dispatchers.IO){
                    taskDao.insertTask(it)
                }
            }
        }
    }

    fun deleteAllTasks(){
        uiScope.launch {
            withContext(Dispatchers.IO) {
                taskDao.deleteAllTasks()
            }
        }
    }

    fun resetCurrentTask(){
        _currentTask.value = null
    }

    fun setCurrentTask(task: Task){
        _currentTask.value = task
    }

    var allHabits = habitDao.getHabits()

}