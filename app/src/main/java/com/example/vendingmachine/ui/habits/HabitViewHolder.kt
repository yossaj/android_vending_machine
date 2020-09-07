package com.example.vendingmachine.ui.habits

import android.graphics.drawable.Animatable
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.vendingmachine.data.models.Habit
import com.example.vendingmachine.databinding.HabitItemBinding


class HabitViewHolder(val binding: HabitItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(habit : Habit){
        binding.habit = habit

        binding.editHabitTitle.setText("Hello Hello")
        binding.repeatHabitMax.setMinValue(1)
        binding.repeatHabitMax.setMaxValue(10)

        binding.habitItemContainer.setOnLongClickListener {
            toggleEditHabit(habit)
            true
        }

        if(habit.count < habit.max) {
            binding.habitTally.visibility = View.VISIBLE
            val tally = habit.count.toString() + "/" + habit.max.toShort()
            binding.habitTally.text = tally
            binding.habitTallyAnimation.visibility = View.INVISIBLE
        }else{
            binding.habitTally.visibility = View.INVISIBLE
            binding.habitTallyAnimation.visibility = View.VISIBLE
            val animation = binding.habitTallyAnimation.drawable as Animatable
            animation.start()
        }
    }

    private fun toggleEditHabit(habit: Habit) {
        if(!binding.editFormContainer.isVisible) {
            binding.habitTitle.visibility = View.INVISIBLE
            binding.incrementCountBtn.visibility = View.INVISIBLE
            binding.decrementCountBtn.visibility = View.INVISIBLE
            binding.habitTally.visibility = View.INVISIBLE
            binding.habitTallyAnimation.visibility = View.INVISIBLE
            binding.editFormContainer.visibility = View.VISIBLE

        }else{
            binding.editFormContainer.visibility = View.GONE
            binding.habitTitle.visibility = View.VISIBLE
            binding.incrementCountBtn.visibility = View.VISIBLE
            binding.decrementCountBtn.visibility = View.VISIBLE
            if(habit.count < habit.max){
                binding.habitTallyAnimation.visibility = View.VISIBLE
            }else{
                binding.habitTally.visibility = View.VISIBLE
            }
        }
    }


}