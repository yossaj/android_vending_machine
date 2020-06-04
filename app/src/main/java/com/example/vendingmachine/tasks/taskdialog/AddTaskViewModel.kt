package com.example.vendingmachine.tasks.taskdialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vendingmachine.data.Task
import com.example.vendingmachine.data.TaskDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AddTaskViewModel(val datasource: TaskDatabase) : ViewModel(){


    val _currentTask = MutableLiveData<Task>()

    val currentTask : LiveData<Task>
        get() = _currentTask

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    fun addTask(){
        uiScope.launch {
            currentTask.value?.let {
                insert(it)
            }
        }
    }

    suspend fun insert(task: Task) {
        datasource.taskDao.insertTask(task)
    }


}