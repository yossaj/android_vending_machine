package com.example.vendingmachine.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vendingmachine.data.Task
import com.example.vendingmachine.data.TaskDao
import kotlinx.coroutines.*

class UserRepository constructor(private val taskDao: TaskDao){

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var allTasks = taskDao.getTasks()

    val _currentTask = MutableLiveData<Task>()
    val currentNewTask : LiveData<Task>
        get() = _currentTask

    val _requestedTask = MutableLiveData<Task>()

    fun updateOnComplete(task: Task){
        uiScope.launch {
            withContext(Dispatchers.IO) {
                updateTask(task)
            }
        }
    }

    suspend fun updateTask(task: Task){
        taskDao.updateTask(task)
    }


    fun deleteTask(){
        uiScope.launch {
            delete()
        }
    }

    suspend fun delete() {
        _currentTask.value?.id?.let {
            withContext(Dispatchers.IO) {
                taskDao.deleteTaskById(it)
            }
        }
    }

    fun addTask(){
        uiScope.launch {
            currentNewTask.value?.let {
                insert(it)
            }
        }
    }

    suspend fun insert(task: Task) {
        withContext(Dispatchers.IO){
            taskDao.insertTask(task)
        }
    }

    fun getTaskToView(){
        uiScope.launch {
            getTask()
        }
    }

    suspend fun getTask(){
        _currentTask.value?.id?.let {
            withContext(Dispatchers.IO) {
                _requestedTask.postValue(taskDao.getTaskById(it))
            }
        }
    }

    fun deleteAllTasks(){
        uiScope.launch {
            deleteAll()
        }
    }

    suspend fun deleteAll(){
        withContext(Dispatchers.IO) {
            taskDao.deleteAllTasks()
        }
    }

    fun resetCurrentTask(){
        _currentTask.value = null
    }

    fun setCurrentTask(task: Task){
        _currentTask.value = task
    }
}