package com.venderino.vendingmachine.ui.tasks

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.venderino.vendingmachine.data.models.Task
import com.venderino.vendingmachine.data.repository.UserRepository

class TasksViewModel@ViewModelInject constructor(
    private val userRepository: UserRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel(){

    val allTasks : LiveData<List<Task>>
        get() = userRepository.allTasks


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

    fun updateTaskWhenComplete(task: Task, boolean: Boolean){
        var updatedtask = task
        updatedtask.completed = boolean
        if(boolean) increaseCoinCount() else decreaseCoinCount()
        userRepository.updateOnComplete(updatedtask)
    }

    fun handleCheckUnCheck(task: Task){
        if(task.completed){
            updateTaskWhenComplete(task, false)
        }else if(!task.completed){
            updateTaskWhenComplete(task, true)
        }
    }


    fun listenForTaskChanges(){
        removeSnapshotListener()
        userRepository.listenForTaskChanges()
    }

    fun removeSnapshotListener(){
        userRepository.removeRegisteredTaskQuery()
    }

    fun increaseCoinCount(){
        userRepository.increaseTokenCount()
    }

    fun decreaseCoinCount(){
        userRepository.reduceTokenCount()
    }


    override fun onCleared() {
        removeSnapshotListener()
        super.onCleared()
    }

}