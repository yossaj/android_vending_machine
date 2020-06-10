package com.example.vendingmachine.tasks.taskdialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vendingmachine.data.Task
import com.example.vendingmachine.data.TaskDatabase
import kotlinx.coroutines.*

class AddTaskViewModel(val datasource: TaskDatabase) : ViewModel(){


    val _currentNewTask = MutableLiveData<Task>()

    val currentNewTask : LiveData<Task>
        get() = _currentNewTask

    val _currentTaskId = MutableLiveData<String>()

    val currentTaskId : LiveData<String>
        get() = _currentTaskId

    val _requestedTask = MutableLiveData<Task>()

    val requestedTask : LiveData<Task>
        get() = _requestedTask


    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun addTask(){
        uiScope.launch {
            currentNewTask.value?.let {
                insert(it)
            }
        }
    }

    fun deleteTask(){
        uiScope.launch {
            delete()
        }
    }

    suspend fun delete() {
        currentTaskId.value?.let {
            withContext(Dispatchers.IO) {
                datasource.taskDao.deleteTaskById(it)
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
            datasource.taskDao.deleteAllTasks()
        }
    }


    suspend fun insert(task: Task) {
        withContext(Dispatchers.IO){
            datasource.taskDao.insertTask(task)
        }

    }

    fun getTaskToView(){
        uiScope.launch {
            getTask()
        }
    }

    suspend fun getTask(){
        currentTaskId.value?.let {
            withContext(Dispatchers.IO) {
                 _requestedTask.postValue(datasource.taskDao.getTaskById(it))
            }
        }
    }



}