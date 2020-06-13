package com.example.vendingmachine.tasks

import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.vendingmachine.R
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
        setHasOptionsMenu(true)
        requireActivity().setTitle(getString(R.string.task_fragment_title))
        sharedPreferences = requireActivity().getSharedPreferences("pref", 0)
        getCoinCount()
        val application = requireNotNull(this.activity).application
        val datasource = TaskDatabase.getInstance(application)
        val factory = TaskViewModelFactory(datasource, application)
        val viewModel = ViewModelProviders.of(this, factory).get(TasksViewModel::class.java)
        val binding = FragmentTasksBinding.inflate(inflater)
        binding.viewmodel = viewModel
        val adapter = TasksAdapter(viewModel)
        viewModel.allTasks.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        binding.taskHabitList.adapter = adapter
        viewModel.uncheckDailyHabits()

        viewModel.coinIncrementSwitch.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it){
                    incrementCoinCount()
                    viewModel._coinIncrementSwitch.value = false
                    adapter.notifyDataSetChanged()
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

        viewModel.navigateToAddHabitTrigger.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    this.findNavController()
                        .navigate(
                            TasksFragmentDirections
                                .actionTasksFragmentToAddTaskFragment(getString(R.string.habit))
                        )
                    viewModel._navigateToAddHabitTrigger.value = false
                }
            }
        })

        viewModel.navigateToViewTaskTrigger.observe(viewLifecycleOwner, Observer {
            viewModel.currentTask.observe(viewLifecycleOwner, Observer {
                it?.let {
                    this.findNavController().navigate(TasksFragmentDirections.actionTasksFragmentToViewTaskFragment(it.id))
                    viewModel.resetUponNavigationToViewTask()
                }
            })
        })
        return binding.root
    }

    fun getCoinCount(){
        coinCount = sharedPreferences.getInt(getString(R.string.coin_count_key), 0)
    }

    fun incrementCoinCount(){
        coinCount = coinCount + 1
        sharedPreferences.edit().putInt(getString(R.string.coin_count_key), coinCount).apply()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.task_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when(id){
            R.id.trash -> {
                this.findNavController()
                    .navigate(TasksFragmentDirections
                        .actionTasksFragmentToAddTaskFragment(getString(R.string.delete_all)))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}