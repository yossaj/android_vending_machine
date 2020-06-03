package com.example.vendingmachine.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.vendingmachine.data.Task
import com.example.vendingmachine.databinding.FragmentTasksBinding

class TasksFragment : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fakeDataList()
        val binding = FragmentTasksBinding.inflate(inflater)
        val adapter = TasksAdapter()
        adapter.submitList(fakeDataList())
        binding.taskHabitList.adapter = adapter
        return binding.root
    }


    fun fakeDataList() : MutableList<Task>{
        val list = mutableListOf<Task>()
        val task1 = Task("New Task", "Some stuff")
        val task2 = Task("Some Task", "Some stuff")
        val task3 = Task("Some Other Task", "Some stuff")
        list.add(task1)
        list.add(task2)
        list.add(task3)
        list.add(task1)
        list.add(task2)
        list.add(task3)
        list.add(task1)
        list.add(task2)
        list.add(task3)
        list.add(task1)
        list.add(task2)
        list.add(task3)
        list.add(task1)
        list.add(task2)
        list.add(task3)
        list.add(task1)
        list.add(task2)
        list.add(task3)
        return list
    }
}