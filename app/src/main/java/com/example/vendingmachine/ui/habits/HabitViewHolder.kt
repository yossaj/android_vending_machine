package com.example.vendingmachine.ui.habits

import android.graphics.drawable.Animatable
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.vendingmachine.data.models.Habit
import com.example.vendingmachine.databinding.HabitItemBinding
import com.example.vendingmachine.utils.AnimationHelper.compressAnimation
import com.example.vendingmachine.utils.AnimationHelper.expandAnimation


class HabitViewHolder(val binding: HabitItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        habit: Habit,
        clickListener: HabitAdapter.OnClickListener
    ) {
        binding.habit = habit
        binding.incrementCountBtn.setOnClickListener { clickListener.onClick(habit, 1) }
        binding.decrementCountBtn.setOnClickListener { clickListener.onClick(habit, 2) }
        binding.deleteHabitBtn.setOnClickListener {
            clickListener.onClick(habit, 6)
            toggleEditHabit(habit)
        }

        binding.editHabitTitle.setText(habit.title)
        binding.repeatHabitMax.setMinValue(1)
        binding.repeatHabitMax.setMaxValue(10)

        binding.updateHabitBtn.setOnClickListener {
            val title = binding.editHabitTitle.editableText.toString()
            val max = binding.repeatHabitMax.value
            if (title.isNotEmpty() && (title != habit.title || max != habit.max)) {
                habit.title = title
                habit.max = max
                clickListener.onClick(habit, 3)
                toggleEditHabit(habit)
            } else {
                clickListener.onClick(habit, 4)
            }
        }



        binding.habitItemContainer.setOnLongClickListener {
            toggleEditHabit(habit)
            true
        }

        binding.habitItemContainer.setOnClickListener {
            clickListener.onClick(habit, 5)
        }

        if (habit.count < habit.max) {
            binding.habitTally.visibility = View.VISIBLE
            val tally = habit.count.toString() + "/" + habit.max.toShort()
            binding.habitTally.text = tally
            binding.habitTallyAnimation.visibility = View.INVISIBLE
        } else {
            binding.habitTally.visibility = View.INVISIBLE
            binding.habitTallyAnimation.visibility = View.VISIBLE
            val animation = binding.habitTallyAnimation.drawable as Animatable
            animation.start()
        }
    }

    private fun toggleEditHabit(habit: Habit) {
        if (!binding.editFormOuterContainer.isVisible) {
            binding.habitTitle.visibility = View.INVISIBLE
            binding.incrementCountBtn.visibility = View.INVISIBLE
            binding.decrementCountBtn.visibility = View.INVISIBLE
            binding.habitTally.visibility = View.INVISIBLE
            binding.habitTallyAnimation.visibility = View.INVISIBLE
            binding.editFormOuterContainer.visibility = View.VISIBLE
            binding.repeatHabitMax.value = habit.max
            expandAnimation(binding.editFormOuterContainer)
        } else {
            binding.editFormOuterContainer.visibility = View.GONE
            binding.habitTitle.visibility = View.VISIBLE
            binding.incrementCountBtn.visibility = View.VISIBLE
            binding.decrementCountBtn.visibility = View.VISIBLE
            if (habit.count < habit.max) {
                binding.habitTally.visibility = View.VISIBLE
            } else {
                binding.habitTallyAnimation.visibility = View.VISIBLE
            }
        }
    }


}