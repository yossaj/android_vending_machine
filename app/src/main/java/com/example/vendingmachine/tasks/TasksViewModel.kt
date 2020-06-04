package com.example.vendingmachine.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vendingmachine.data.Task
import com.example.vendingmachine.data.TaskDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class TasksViewModel(val datasource: TaskDatabase) : ViewModel(){

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val allTasks = datasource.taskDao.getTasks()

    val _coinIncrementSwitch = MutableLiveData<Boolean>()

    val coinIncrementSwitch : LiveData<Boolean>
        get() = _coinIncrementSwitch

    val _navigateToAddTaskTrigger = MutableLiveData<Boolean>()

    val navigateToAddTaskTrigger : LiveData<Boolean>
        get() = _navigateToAddTaskTrigger


    fun incrementCoinSwitch(){
        _coinIncrementSwitch.value = true
    }

    fun triggerAddTaskNav(){
        _navigateToAddTaskTrigger.value = true
    }


    init {
        _coinIncrementSwitch.value = false
        _navigateToAddTaskTrigger.value = false

    }

}