package com.example.vendingmachine.ui.tasks.taskdialog

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.vendingmachine.data.models.Task
import com.example.vendingmachine.data.repository.UserRepository

class AddTaskViewModel@ViewModelInject constructor(private val userRepository: UserRepository, @Assisted private val savedStateHandle: SavedStateHandle) : ViewModel(){


    val currentTask : LiveData<Task>
        get() = userRepository._currentTask




    fun setCurrentTask(task: Task){
        userRepository.setCurrentTask(task)
    }

    fun getUserId() : String{
       return userRepository.getUid()
    }

    fun deleteTask(){
        userRepository.deleteTask()
    }

    fun deleteAllTasks(){
        userRepository.deleteAllTasks()
    }


}