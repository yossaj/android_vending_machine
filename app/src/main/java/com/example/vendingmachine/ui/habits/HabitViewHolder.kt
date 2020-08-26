package com.example.vendingmachine.ui.habits

import androidx.recyclerview.widget.RecyclerView
import com.example.vendingmachine.data.models.Habit
import com.example.vendingmachine.databinding.HabitItemBinding

class HabitViewHolder(val binding: HabitItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(habit : Habit){
        binding.habit = habit
        val tally = habit.count.toString() + "/" + habit.max.toShort()
        binding.habitTally.text = tally
    }


}