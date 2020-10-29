package com.venderino.vendingmachine.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.animation.doOnEnd
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.venderino.vendingmachine.R
import com.venderino.vendingmachine.data.models.Task
import com.venderino.vendingmachine.databinding.FragmentTasksBinding
import com.venderino.vendingmachine.utils.AnimationHelper.arrowGrow
import com.venderino.vendingmachine.utils.AnimationHelper.arrowShrink
import com.venderino.vendingmachine.utils.AnimationHelper.compressAnimation
import com.venderino.vendingmachine.utils.AnimationHelper.expandAnimation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_add_task.*

@AndroidEntryPoint
class TasksFragment : Fragment() {

    private val viewModel: TasksViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        requireActivity().setTitle(getString(R.string.task_fragment_title))
        val binding = FragmentTasksBinding.inflate(inflater)
        binding.viewmodel = viewModel
        val adapter = TasksAdapter(TasksAdapter.OnClickListener{
            task, viewType ->
            when(viewType){
                1 -> viewModel.deleteTask(task)
                2 -> viewModel.handleCheckUnCheck(task)
                3 -> viewModel.updateTasks(task)
                4 -> Toast.makeText(requireContext(), "Nothing to Update", Toast.LENGTH_SHORT).show()
            }
        })

        addTaskFormSetup(binding, adapter)
        binding.showTaskFormButton.setOnClickListener{
            if (binding.addTaskOuterContainer.isVisible){
                binding.addTaskOuterContainer.visibility = View.GONE
            }else{
                binding.addTaskOuterContainer.visibility = View.VISIBLE
                expandAnimation(binding.addTaskOuterContainer)
            }
        }

        viewModel.period.observe(viewLifecycleOwner, Observer {
            viewModel.listenForTaskChanges()
            when(it){
                1 ->{
                    binding.periodText.text = "TODAY"
                    arrowGrow(binding.incrementArrowBtn)
                    binding.taskList.scheduleLayoutAnimation()
                    arrowShrink(binding.decrementArrowBtn)
                }
                2 -> {
                    binding.periodText.text = "THIS WEEK"
                    arrowGrow(binding.decrementArrowBtn)
                    arrowGrow(binding.incrementArrowBtn)
                    binding.taskList.scheduleLayoutAnimation()
                }
                3 -> {
                    binding.periodText.text = "THIS MONTH"
                    arrowShrink(binding.incrementArrowBtn)
                    binding.taskList.scheduleLayoutAnimation()
                }
            }

        })

        viewModel.allTasks.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            binding.emptyTaskError.visibility = if(it.isEmpty()) View.VISIBLE else View.GONE
        })

        binding.taskList.adapter = adapter

        return binding.root
    }

    private fun addTaskFormSetup(
        binding: FragmentTasksBinding,
        adapter: TasksAdapter
    ) {
        var color = 2
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

        binding.addTaskBtn.setOnClickListener {
            var taskname = binding.taskEditText.editableText.toString()
            var taskNote = binding.taskNotesEditText.editableText.toString()
            var period = periodStringToValue(binding.periodPicker.selectedItem.toString())
            if(taskname.isNullOrEmpty() || taskNote.isNullOrEmpty()){
                Toast.makeText(
                    requireContext(),
                    "Please fill in all fields",
                    Toast.LENGTH_SHORT
                ).show()
            }else{
                val newTask = Task(taskname, taskNote, period, color)
                viewModel.addTask(newTask)

                val compressAnimation = compressAnimation(binding.addTaskOuterContainer)
                compressAnimation.start()
                compressAnimation.doOnEnd {
                    binding.addTaskOuterContainer.visibility = View.GONE
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    fun periodStringToValue(period: String): Int {
        when (period) {
            "Today" -> return 1
            "This Week" -> return 2
            "This Month" -> return 3
            else -> return 1
        }
    }
}