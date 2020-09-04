package com.example.vendingmachine.ui.tasks

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import android.view.animation.OvershootInterpolator
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.animation.doOnEnd
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.vendingmachine.R
import com.example.vendingmachine.data.models.Task
import com.example.vendingmachine.databinding.FragmentTasksBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_add_task.select_blue_task
import kotlinx.android.synthetic.main.dialog_add_task.select_green_task
import kotlinx.android.synthetic.main.dialog_add_task.select_yellow_task

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
                val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0.5f, 1f)
                val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.5f, 1f)
                val alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f)
                ObjectAnimator.ofPropertyValuesHolder(binding.addTaskOuterContainer, scaleY, scaleX, alpha).apply {
                    interpolator = OvershootInterpolator()
                }.start()
            }
        }

        viewModel.period.observe(viewLifecycleOwner, Observer {
            viewModel.listenForTaskChanges()
            when(it){
                1 -> binding.periodText.text = "TODAY"
                2 -> binding.periodText.text = "THIS WEEK"
                3 -> binding.periodText.text = "THIS MONTH"
            }

            val scaleXIncrease = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1.3f)
            val scaleYIncrease = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1.3f)
            val scaleXDecrease = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.3f, 1f)
            val scaleYDecrease = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.3f, 1f)


            if(it == 1 ) {
                ObjectAnimator.ofPropertyValuesHolder(binding.incrementArrowBtn, scaleYIncrease, scaleXIncrease)
                    .apply {
                        interpolator = OvershootInterpolator()
                    }.start()

                binding.taskList.scheduleLayoutAnimation()

                ObjectAnimator.ofPropertyValuesHolder(binding.decrementArrowBtn, scaleYDecrease, scaleXDecrease)
                    .apply {
                        interpolator = OvershootInterpolator()
                    }.start()
            }else if(it == 3){
                ObjectAnimator.ofPropertyValuesHolder(binding.incrementArrowBtn, scaleYDecrease, scaleXDecrease)
                    .apply {
                        interpolator = OvershootInterpolator()
                    }.start()
                binding.taskList.scheduleLayoutAnimation()
            }else if(it == 2){
                ObjectAnimator.ofPropertyValuesHolder(binding.decrementArrowBtn, scaleYIncrease, scaleXIncrease)
                    .apply {
                        interpolator = OvershootInterpolator()
                    }.start()
                binding.taskList.scheduleLayoutAnimation()

            }



        })

        viewModel.allTasks.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)

        })

        binding.taskList.adapter = adapter

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

                val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 0.0f)
                val alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 1f, 0.0f)
                val animator = ObjectAnimator.ofPropertyValuesHolder(
                    binding.addTaskOuterContainer,
                    scaleY,
                    alpha
                ).apply {
                    interpolator = OvershootInterpolator()
                }
                animator.start()
                animator.doOnEnd {
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