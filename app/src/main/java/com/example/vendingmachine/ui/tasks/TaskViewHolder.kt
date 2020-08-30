package com.example.vendingmachine.ui.tasks

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.graphics.Color
import android.graphics.Paint
import android.text.Editable
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.vendingmachine.data.models.Task
import com.example.vendingmachine.databinding.TaskItemBinding

class TaskViewHolder(val binding: TaskItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(task: Task) {
        binding.task = task
        binding.taskItemTitle.text = task.title
        binding.taskItemContainer.setCardBackgroundColor(getTaskColour(task))

        binding.taskItemContainer.setOnClickListener {
            if (binding.expandedTaskNote.isVisible) {
                binding.expandedTaskNote.visibility = View.GONE
            } else {
                binding.expandedTaskNote.visibility = View.VISIBLE
                val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.5f, 1f)
                val alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f)
                ObjectAnimator.ofPropertyValuesHolder(binding.expandedTaskNote, scaleY, alpha).apply {
                    interpolator = AccelerateDecelerateInterpolator()
                }.start()
            }
        }

        binding.editTaskButton.setOnClickListener{
            if(!binding.editTaskNoteText.isVisible){
                binding.editTaskText.visibility = View.VISIBLE
                binding.editTaskText.setText(task.title)
                binding.editTaskNoteText.visibility = View.VISIBLE
                binding.editTaskNoteText.setText(task.description)
                binding.taskItemTitle.visibility = View.GONE
                binding.expandedTaskNote.visibility = View.GONE
                binding.deleteTaskBtn.visibility = View.VISIBLE
            }else{
                binding.taskItemTitle.visibility = View.VISIBLE
                binding.expandedTaskNote.visibility = View.VISIBLE
                binding.editTaskNoteText.visibility = View.GONE
                binding.editTaskText.visibility = View.GONE
                binding.deleteTaskBtn.visibility = View.GONE
            }
        }

        if (task.isCompleted) {
            binding.taskItemTitle.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            binding.taskItemCheckbox.isChecked = true
        } else if (!task.isCompleted) {
            binding.taskItemCheckbox.isChecked = false
            binding.taskItemTitle.paintFlags = 0
        }
    }

    fun getTaskColour(task: Task): Int {
        when (task.colour) {
            1 -> return Color.parseColor("#51C1E4")
            2 -> return Color.parseColor("#F2C94C")
            3 -> return Color.parseColor("#6FCF97")
            else -> return Color.parseColor("#6FCF97")
        }
    }
}