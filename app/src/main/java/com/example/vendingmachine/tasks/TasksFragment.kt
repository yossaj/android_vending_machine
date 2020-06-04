package com.example.vendingmachine.tasks

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.vendingmachine.R
import com.example.vendingmachine.data.Task
import com.example.vendingmachine.data.TaskDatabase
import com.example.vendingmachine.databinding.FragmentTasksBinding

class TasksFragment : Fragment(){

    lateinit var sharedPreferences: SharedPreferences
    var coinCount = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences = requireActivity().getSharedPreferences("pref", 0)
        getCoinCount()
        val application = requireNotNull(this.activity).application
        val datasource = TaskDatabase.getInstance(application)
        val factory = TaskViewModelFactory(datasource)
        val viewModel = ViewModelProviders.of(this, factory).get(TasksViewModel::class.java)
        val binding = FragmentTasksBinding.inflate(inflater)
        binding.viewmodel = viewModel
        val adapter = TasksAdapter(viewModel)
        viewModel.allTasks.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        binding.taskHabitList.adapter = adapter

        viewModel.coinIncrementSwitch.observe(viewLifecycleOwner, Observer {
            it?.let {
                val switch = it
                if(it){
                    incrementCoinCount()
                    viewModel._coinIncrementSwitch.value = false
                }
            }
        })

        viewModel.navigateToAddTaskTrigger.observe(viewLifecycleOwner, Observer {
            it?.let{
                if(it) {
                    this.findNavController()
                        .navigate(TasksFragmentDirections.actionTasksFragmentToAddTaskFragment())
                    viewModel._navigateToAddTaskTrigger.value = false
                }
            }
        })
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

    fun getCoinCount(){
        coinCount = sharedPreferences.getInt(getString(R.string.coin_count_key), 0)
    }

    fun incrementCoinCount(){
        coinCount = coinCount + 1
        sharedPreferences.edit().putInt(getString(R.string.coin_count_key), coinCount).apply()
    }
}