package com.example.vendingmachine.ui.tasks.taskdialog

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.vendingmachine.data.Task
import com.example.vendingmachine.data.TaskDatabase
import com.example.vendingmachine.data.repository.UserRepository
import kotlinx.coroutines.*

class AddTaskViewModel@ViewModelInject constructor(private val userRepository: UserRepository, @Assisted private val savedStateHandle: SavedStateHandle) : ViewModel(){



    val _currentTaskId = MutableLiveData<String>()

    val currentTaskId : LiveData<String>
        get() = _currentTaskId

    val currentTask : LiveData<Task>
        get() = userRepository._currentTask

    val requestedTask : LiveData<Task>
        get() = userRepository._requestedTask

    fun addTask(){
        userRepository.addTask()
    }

    fun setCurrentTask(task: Task){
        userRepository.setCurrentTask(task)
    }

    fun deleteTask(){
        userRepository.deleteTask()
    }

    fun getTaskToView(){
        userRepository.getTaskToView()
    }


    fun deleteAllTasks(){
        userRepository.deleteAllTasks()
    }


}