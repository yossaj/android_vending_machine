package com.example.vendingmachine.ui.habits

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.vendingmachine.data.models.Habit
import com.example.vendingmachine.databinding.HabitItemBinding

class HabitAdapter(val clickListener : OnClickListener) : ListAdapter<Habit, HabitViewHolder>(HabitDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = HabitItemBinding.inflate(layoutInflater, parent, false)
        return HabitViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        val habit = getItem(position)
        return holder.bind(habit, clickListener)
    }

    class HabitDiffCallback() : DiffUtil.ItemCallback<Habit>(){
        override fun areItemsTheSame(oldItem: Habit, newItem: Habit): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Habit, newItem: Habit): Boolean {
            return oldItem.equals(newItem)
        }

    }

    class OnClickListener(val clickListener: (habit: Habit, viewType : Int) -> Unit) {
        fun onClick(habit: Habit, viewType : Int) = clickListener(habit, viewType)
    }


}