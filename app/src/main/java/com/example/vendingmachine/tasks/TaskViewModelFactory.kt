package com.example.vendingmachine.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vendingmachine.data.TaskDatabase

class TaskViewModelFactory(val datasource: TaskDatabase) : ViewModelProvider.Factory{
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TasksViewModel::class.java)) {
            return TasksViewModel(datasource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}