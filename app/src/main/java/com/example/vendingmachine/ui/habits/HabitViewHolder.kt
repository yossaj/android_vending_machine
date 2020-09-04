package com.example.vendingmachine.ui.habits

import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.example.vendingmachine.R
import com.example.vendingmachine.data.models.Habit
import com.example.vendingmachine.databinding.HabitItemBinding


class HabitViewHolder(val binding: HabitItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(habit : Habit){
        binding.habit = habit
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


}