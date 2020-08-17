package com.example.vendingmachine.ui.tasks

import android.app.Application
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.work.WorkManager
import com.example.vendingmachine.data.Task
import com.example.vendingmachine.data.TaskDao
import com.example.vendingmachine.data.TaskDatabase
import com.example.vendingmachine.data.repository.ApiRepository
import kotlinx.coroutines.*

class TasksViewModel@ViewModelInject constructor(
    private val taskDao: TaskDao,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel(){

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var allTasks = taskDao.getTasks()

    val _coinIncrementSwitch = MutableLiveData<Boolean>()

    val coinIncrementSwitch : LiveData<Boolean>
        get() = _coinIncrementSwitch

    val _navigateToAddTaskTrigger = MutableLiveData<Boolean>()

    val navigateToAddTaskTrigger : LiveData<Boolean>
        get() = _navigateToAddTaskTrigger

    val _navigateToAddHabitTrigger = MutableLiveData<Boolean>()

    val navigateToAddHabitTrigger : LiveData<Boolean>
        get() = _navigateToAddHabitTrigger

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
        if(boolean){incrementCoinSwitch()}
        uiScope.launch {
            withContext(Dispatchers.IO) {
                updateTask(updatedtask)
            }
        }
    }

    fun updateHabitWhenComplete(task: Task, boolean: Boolean){
        var updateHabit = task
        updateHabit.habitCount += 1
        updateHabit.isCompleted = boolean
        updateHabit.updatedTime = System.currentTimeMillis()
        if(boolean){incrementCoinSwitch()}
        uiScope.launch {
            withContext(Dispatchers.IO) {
                updateTask(updateHabit)
            }
        }

    }

    suspend fun updateTask(task: Task){
        taskDao.updateTask(task)
    }

    fun handleCheckUnCheck(task: Task){
        if(task.habit && !task.isCompleted){
            updateHabitWhenComplete(task, true )
        }
        else if(task.isCompleted){
            updateTaskWhenComplete(task, false)
        }else if(!task.isCompleted){
            updateTaskWhenComplete(task, true)
        }
    }



    fun triggerAddHabitNav(){
        _navigateToAddHabitTrigger.value = true
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