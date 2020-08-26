package com.example.vendingmachine.ui.habits

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import com.example.vendingmachine.R
import com.example.vendingmachine.data.models.Habit
import com.example.vendingmachine.databinding.FragmentHabitBinding


class HabitFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHabitBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        val adapter = HabitAdapter()
        binding.habitList.adapter = adapter
        val habitList = listOf<Habit>(
            Habit("Beep", 3, 2),
            Habit("Bop", 3, 1),
            Habit("Bup", 3, 0)
        )
        adapter.submitList(habitList)

        binding.showHabitFromBtn.setOnClickListener{
            if(binding.addHabitOuterContainer.isVisible){
                binding.addHabitOuterContainer.visibility = View.GONE
            }else{
                binding.addHabitOuterContainer.visibility = View.VISIBLE
            }


        }

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

        return binding.root
    }

}