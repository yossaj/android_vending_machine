package com.example.vendingmachine.ui.tasks

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.view.animation.OvershootInterpolator
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.vendingmachine.R
import com.example.vendingmachine.databinding.FragmentTasksBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_add_task.*

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

        addTaskFormSetup(binding)
        binding.showTaskFormButton.setOnClickListener{
            if (binding.addTaskOuterContainer.isVisible){
                binding.addTaskOuterContainer.visibility = View.GONE
            }else{
                binding.addTaskOuterContainer.visibility = View.VISIBLE
                val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0.5f, 1f)
                val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.5f, 1f)
                val alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f)
                ObjectAnimator.ofPropertyValuesHolder(binding.addTaskOuterContainer, scaleY, scaleX, alpha).apply {
                    interpolator = OvershootInterpolator()
                }.start()
            }

        }

        viewModel.period.observe(viewLifecycleOwner, Observer {
            viewModel.updateTasks()
            when(it){
                1 -> binding.periodText.text = "TODAY"
                2 -> binding.periodText.text = "THIS WEEK"
                3 -> binding.periodText.text = "THIS MONTH"
            }
        })


        viewModel.allTasks.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
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

    private fun addTaskFormSetup(binding: FragmentTasksBinding) {
        var color = 1
        binding.selectBlueTask.setOnClickListener {
            color = 1
            it.isSelected = true
            select_green_task.isSelected = false
            select_yellow_task.isSelected = false
            binding.addTaskOuterContainer.setBackgroundResource(R.drawable.curved_corners_background_blue)
        }

        binding.selectYellowTask.setOnClickListener {
            color = 2
            it.isSelected = true
            select_green_task.isSelected = false
            select_blue_task.isSelected = false
            binding.addTaskOuterContainer.setBackgroundResource(R.drawable.curved_corners_background_yellow)
        }

        binding.selectGreenTask.setOnClickListener {
            color = 3
            it.isSelected = true
            select_blue_task.isSelected = false
            select_yellow_task.isSelected = false
            binding.addTaskOuterContainer.setBackgroundResource(R.drawable.curved_corners_background_green)
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.period_array,
            android.R.layout.simple_spinner_item
        ).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.periodPicker.adapter = arrayAdapter
        }
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