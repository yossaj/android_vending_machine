package com.example.vendingmachine.ui.tasks

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.vendingmachine.data.models.Task
import com.example.vendingmachine.data.repository.UserRepository

class TasksViewModel@ViewModelInject constructor(
    private val userRepository: UserRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel(){

    val allTasks : LiveData<List<Task>>
        get() = userRepository.allTasks

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

    val period : LiveData<Int>
        get() = userRepository.period


    fun addTask(task: Task){
        userRepository.addTask(task)
    }

    fun updateTasks(task: Task){
        userRepository.updateTask(task)
    }

    fun deleteTask(task: Task){
        userRepository.deleteTask(task)
    }

    fun incrementPeriod(){
        userRepository.incrementPeriod()
    }

    fun decrementPeriod(){
        userRepository.decrementPeriod()
    }


    fun incrementCoinSwitch(){
        _coinIncrementSwitch.value = true
    }

    fun updateTaskWhenComplete(task: Task, boolean: Boolean){
        var updatedtask = task
        updatedtask.isCompleted = boolean
        if(boolean){incrementCoinSwitch()}
        userRepository.updateOnComplete(updatedtask)
    }

    fun handleCheckUnCheck(task: Task){
        if(task.isCompleted){
            updateTaskWhenComplete(task, false)
        }else if(!task.isCompleted){
            updateTaskWhenComplete(task, true)
        }
    }

    fun resetViewTaskTrigger(){
        _navigateToViewTaskTrigger.value = false
    }

    fun resetUponNavigationToViewTask(){
        resetViewTaskTrigger()
    }

    fun listenForTaskChanges(){
        removeSnapshotListener()
        userRepository.listenForTaskChanges()
    }

    fun removeSnapshotListener(){
        userRepository.removeRegisteredQuery()
    }

    init {
        _coinIncrementSwitch.postValue(false)
        _navigateToAddTaskTrigger.postValue(false)
        _navigateToViewTaskTrigger.postValue(false)
    }

    override fun onCleared() {
        removeSnapshotListener()
        super.onCleared()
    }

}