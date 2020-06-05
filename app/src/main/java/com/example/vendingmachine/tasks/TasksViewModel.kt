package com.example.vendingmachine.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vendingmachine.data.Task
import com.example.vendingmachine.data.TaskDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TasksViewModel(val datasource: TaskDatabase) : ViewModel(){

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    val allTasks = datasource.taskDao.getTasks()

    val _coinIncrementSwitch = MutableLiveData<Boolean>()

    val coinIncrementSwitch : LiveData<Boolean>
        get() = _coinIncrementSwitch

    val _navigateToAddTaskTrigger = MutableLiveData<Boolean>()

    val navigateToAddTaskTrigger : LiveData<Boolean>
        get() = _navigateToAddTaskTrigger

    val _navigateToViewTaskTrigger = MutableLiveData<Boolean>()

    val navigateToViewTaskTrigger : LiveData<Boolean>
        get() = _navigateToViewTaskTrigger

    val _currentTask = MutableLiveData<Task>()

    val currentTask : LiveData<Task>
        get() = _currentTask



    fun incrementCoinSwitch(){
        _coinIncrementSwitch.value = true
    }


    fun updateTaskWhenComplete(task: Task){
        val updatedtask = task
        updatedtask.isCompleted = true
        incrementCoinSwitch()
        uiScope.launch {
            updateTask(updatedtask)
        }

    }

    suspend fun updateTask(task: Task){
        datasource.taskDao.updateTask(task)
    }

    fun triggerAddTaskNav(){
        _navigateToAddTaskTrigger.value = true
    }

    fun triggerViewTaskNav(){
        _navigateToViewTaskTrigger.value = true
    }

    fun navigateToViewTaskAndPassTask(task: Task){
        triggerViewTaskNav()
        _currentTask.value = task
    }


    init {
        _coinIncrementSwitch.value = false
        _navigateToAddTaskTrigger.value = false

    }

}