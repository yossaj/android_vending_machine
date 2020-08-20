package com.example.vendingmachine.ui.tasks

import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.vendingmachine.R
import com.example.vendingmachine.databinding.FragmentTasksBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TasksFragment : Fragment() {

    lateinit var sharedPreferences: SharedPreferences
    private val viewModel: TasksViewModel by viewModels()

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
        val binding = FragmentTasksBinding.inflate(inflater)
        binding.viewmodel = viewModel
        val adapter = TasksAdapter(viewModel)
        viewModel.allTasks.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            viewModel.syncFireStore()
        })

        binding.taskHabitList.adapter = adapter

        viewModel.coinIncrementSwitch.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    incrementCoinCount()
                    viewModel._coinIncrementSwitch.value = false
                    adapter.notifyDataSetChanged()
                }
            }
        })

        viewModel.navigateToAddTaskTrigger.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
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
            if (it) {
                this.findNavController().navigate(
                    TasksFragmentDirections.actionTasksFragmentToViewTaskFragment()
                )
                viewModel.resetUponNavigationToViewTask()
            }
        }

        )
        return binding.root
    }

    fun getCoinCount() {
        coinCount = sharedPreferences.getInt(getString(R.string.coin_count_key), 0)
    }

    fun incrementCoinCount() {
        coinCount = coinCount + 1
        sharedPreferences.edit().putInt(getString(R.string.coin_count_key), coinCount).apply()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.task_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.trash -> {
                this.findNavController()
                    .navigate(
                        TasksFragmentDirections
                            .actionTasksFragmentToAddTaskFragment(getString(R.string.delete_all))
                    )
            }
        }
        return super.onOptionsItemSelected(item)
    }
}