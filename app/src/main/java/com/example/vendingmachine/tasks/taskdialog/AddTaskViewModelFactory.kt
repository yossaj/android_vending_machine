package com.example.vendingmachine.tasks.taskdialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vendingmachine.data.TaskDatabase


class AddTaskViewModelFactory(val datasource: TaskDatabase) : ViewModelProvider.Factory{
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddTaskViewModel::class.java)) {
            return AddTaskViewModel(datasource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}