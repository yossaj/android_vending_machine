package com.venderino.vendingmachine.ui.habits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.venderino.vendingmachine.R
import com.venderino.vendingmachine.data.models.Habit
import com.venderino.vendingmachine.databinding.FragmentHabitBinding
import com.venderino.vendingmachine.utils.AnimationHelper.arrowGrow
import com.venderino.vendingmachine.utils.AnimationHelper.arrowShrink
import com.venderino.vendingmachine.utils.AnimationHelper.expandAnimation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HabitFragment : Fragment() {

    private val viewModel: HabitsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHabitBinding.inflate(layoutInflater)
        val adapter = HabitAdapter(HabitAdapter.OnClickListener { habit, viewType ->
            if (viewType.equals(1) && habit.count < habit.max) {
                val increaseCount = habit.count + 1
                incrementCoinCount()
                viewModel.updateHabitCount(habit, increaseCount)
            } else if (viewType.equals(2) && habit.count > 0) {
                val decreaseCount = habit.count - 1
                viewModel.updateHabitCount(habit, decreaseCount)
            }else if(viewType.equals(3)){
                viewModel.updateHabit(habit)
            }else if(viewType.equals(4)){
                Toast.makeText(requireContext(), "Nothing to Update", Toast.LENGTH_SHORT).show()
            }else if(viewType.equals(5)){
                Toast.makeText(requireContext(), "Long Click to Edit Habit", Toast.LENGTH_SHORT).show()
            }else if(viewType.equals(6)){
                viewModel.deleteHabit(habit)
            }
        })
        binding.habitList.adapter = adapter
        viewModel.allHabits.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            viewModel.resetDailyHabits()
        })
        setUpShowHabitFormBtn(binding)
        setUpAddHabitForm(binding)
        binding.incrementArrowBtnHabit.setOnClickListener { viewModel.incrementFrequency() }
        binding.decrementArrowBtnHabit.setOnClickListener { viewModel.decrementFrequency() }
        viewModel.period.observe(viewLifecycleOwner, Observer {
            viewModel.listenForHabits()
            binding.habitFrequency.text = periodValueToString(it)
            when(it){
                1 -> {
                    arrowGrow(binding.incrementArrowBtnHabit)
                    arrowShrink(binding.decrementArrowBtnHabit)}
                2 -> {
                    arrowGrow(binding.incrementArrowBtnHabit)
                    arrowGrow(binding.decrementArrowBtnHabit)
                }
                3 -> {
                    arrowShrink(binding.incrementArrowBtnHabit)
                    arrowGrow(binding.decrementArrowBtnHabit)
                }
            }
        })

        return binding.root
    }

    private fun setUpShowHabitFormBtn(binding: FragmentHabitBinding) {
        binding.showHabitFromBtn.setOnClickListener {
            if (binding.addHabitOuterContainer.isVisible) {
                binding.addHabitOuterContainer.visibility = View.GONE
            } else {
                binding.addHabitOuterContainer.visibility = View.VISIBLE
                expandAnimation(binding.addHabitOuterContainer)
            }
        }
    }

    private fun setUpAddHabitForm(binding: FragmentHabitBinding) {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.frequecy_array,
            android.R.layout.simple_spinner_item
        ).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.frequencyPicker.adapter = arrayAdapter
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.times_array,
            android.R.layout.simple_spinner_item
        ).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.timesPicker.adapter = arrayAdapter
        }

        binding.addHabitBtn.setOnClickListener {
            var habitname = binding.habitEditText.editableText.toString()
            var frequency = periodStringToValue(binding.frequencyPicker.selectedItem.toString())
            var times = timesStringToValue(binding.timesPicker.selectedItem.toString())
            if (habitname.isNullOrEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please fill in all fields",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val newhabit = Habit(habitname, times, frequency)
                viewModel.addHabit(newhabit)
                binding.addHabitOuterContainer.visibility = View.GONE
            }
        }
    }

    fun periodStringToValue(period: String): Int {
        when(period) {
            "Daily" -> return 1
            "Weekly" -> return 2
            "Monthly" -> return 3
            else -> return 1
        }
    }

    fun periodValueToString(frequency: Int) : String{
        when(frequency){
            1 -> return "Daily"
            2 -> return "Weekly"
            3 -> return "Monthly"
            else -> return "Daily"
        }
    }

    fun timesStringToValue(times: String): Int {
        when (times) {
            "Once" -> return 1
            "Twice" -> return 2
            "Three Times" -> return 3
            "Four Times" -> return 4
            "Five Times" -> return 5
            "Six Times" -> return 6
            "Seven Times" -> return 7
            else -> return 1
        }
    }

    fun incrementCoinCount() {
        val sharedPreferences = requireActivity().getSharedPreferences("pref", 0)
        var coinCount = sharedPreferences.getInt(getString(R.string.coin_count_key), 0)
        coinCount += 1
        sharedPreferences.edit().putInt(getString(R.string.coin_count_key), coinCount).apply()
    }

}