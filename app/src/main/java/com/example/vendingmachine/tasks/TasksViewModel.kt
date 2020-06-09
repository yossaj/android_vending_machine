package com.example.vendingmachine.tasks

import android.view.View
import android.widget.CheckBox
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vendingmachine.data.Task
import com.example.vendingmachine.data.TaskDatabase
import kotlinx.coroutines.*

class TasksViewModel(val datasource: TaskDatabase) : ViewModel(){

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var allTasks = datasource.taskDao.getTasks()

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



    fun updateTaskWhenComplete(task: Task, boolean: Boolean){
        var updatedtask = task
        updatedtask.isCompleted = boolean
        incrementCoinSwitch()
        uiScope.launch {
            withContext(Dispatchers.IO) {
                updateTask(updatedtask)
            }
        }
    }

    suspend fun updateTask(task: Task){
        datasource.taskDao.updateTask(task)
    }

    fun handleCheckUnCheck(task: Task){
        if(task.isCompleted){
            updateTaskWhenComplete(task, false)
        }else if(!task.isCompleted){
            updateTaskWhenComplete(task, true)
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

    fun triggerAddTaskNav(){
        _navigateToAddTaskTrigger.value = true
    }

    fun triggerViewTaskNav(){
        _navigateToViewTaskTrigger.value = true
    }

    fun resetViewTaskTrigger(){
        _navigateToViewTaskTrigger.value = false
    }

    fun navigateToViewTaskAndPassTask(task: Task){
        triggerViewTaskNav()
        _currentTask.value = task
    }

    fun resetCurrentTask(){
        _currentTask.value = null
    }

    fun resetUponNavigationToViewTask(){
        resetCurrentTask()
        resetViewTaskTrigger()
    }

    init {
        _coinIncrementSwitch.postValue(false)
        _navigateToAddTaskTrigger.postValue(false)
    }

}