package com.example.vendingmachine.ui.habits

import android.graphics.drawable.Animatable
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.vendingmachine.data.models.Habit
import com.example.vendingmachine.databinding.HabitItemBinding


class HabitViewHolder(val binding: HabitItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        habit: Habit,
        clickListener: HabitAdapter.OnClickListener
    ){
        binding.habit = habit
        binding.incrementCountBtn.setOnClickListener{clickListener.onClick(habit, 1)}
        binding.decrementCountBtn.setOnClickListener{clickListener.onClick(habit, 2)}

        binding.editHabitTitle.setText(habit.title)
        binding.repeatHabitMax.setMinValue(1)
        binding.repeatHabitMax.setMaxValue(10)




            binding.updateHabitBtn.setOnClickListener {
                val title = binding.editHabitTitle.editableText.toString()
                val max = binding.repeatHabitMax.value
                if(title.isNotEmpty() && title != habit.title) {
                    habit.title = title
                    habit.max = max
                    clickListener.onClick(habit, 3)
                    toggleEditHabit(habit)
                }else{
                    clickListener.onClick(habit, 4)
                }
            }



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
            binding.repeatHabitMax.value = habit.max
        }else{
            binding.editFormContainer.visibility = View.GONE
            binding.habitTitle.visibility = View.VISIBLE
            binding.incrementCountBtn.visibility = View.VISIBLE
            binding.decrementCountBtn.visibility = View.VISIBLE
            if(habit.count < habit.max){
                binding.habitTally.visibility = View.VISIBLE
            }else{
                binding.habitTallyAnimation.visibility = View.VISIBLE
            }
        }
    }


}