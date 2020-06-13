package com.example.vendingmachine.tasks

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.vendingmachine.data.Task
import com.example.vendingmachine.data.TaskDatabase
import com.example.vendingmachine.workers.DailyHabitReset
import kotlinx.coroutines.*
import java.sql.Time
import java.util.concurrent.TimeUnit

class TasksViewModel(
    val datasource: TaskDatabase,
    application: Application
) : ViewModel(){

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val workManager = WorkManager.getInstance(application)

    var allTasks = datasource.taskDao.getTasks()

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


    internal fun uncheckDailyHabits(){
        val resetHabitRequest = PeriodicWorkRequestBuilder<DailyHabitReset>(60, TimeUnit.SECONDS)
        val builtRequest = resetHabitRequest.addTag("Reset Habit Request").build()
        workManager
            .enqueueUniquePeriodicWork(
                "reset_habit_worker",
                ExistingPeriodicWorkPolicy.KEEP,
                builtRequest)
    }



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
        if(boolean){incrementCoinSwitch()}
        uiScope.launch {
            withContext(Dispatchers.IO) {
                updateTask(updateHabit)
            }
        }

    }

    suspend fun updateTask(task: Task){
        datasource.taskDao.updateTask(task)
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