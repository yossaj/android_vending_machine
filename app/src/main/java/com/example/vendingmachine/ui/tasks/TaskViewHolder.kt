package com.example.vendingmachine.ui.tasks

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.vendingmachine.data.models.Task
import com.example.vendingmachine.databinding.TaskItemBinding

class TaskViewHolder(val binding: TaskItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        task: Task,
        clickListener: TasksAdapter.OnClickListener
    ) {
        binding.task = task
        binding.taskItemTitle.text = task.title
        binding.expandedTaskNote.text = task.description
        binding.taskItemContainer.setCardBackgroundColor(getTaskColour(task))

        binding.deleteTaskBtn.setOnClickListener{
            clickListener.onClick(task, 1)
            toggleEditableText(task)
        }
        binding.taskItemCheckbox.setOnClickListener{ clickListener.onClick(task, 2) }

        binding.taskItemContainer.setOnClickListener {
            toggleExpanded()
        }

        binding.editTaskButton.setOnClickListener{
            toggleEditableText(task)
        }

        binding.updateTaskBtn.setOnClickListener{
            val title = binding.editTaskText.editableText.toString()
            val notes =  binding.editTaskNoteText.editableText.toString()

            if (title.isNotBlank() && (title != task.title || notes != task.description)){
                task.title = title
                task.description = notes
                clickListener.onClick(task, 3)
                binding.taskItemTitle.setText(task.title)
                binding.expandedTaskNote.setText(task.description)
                toggleEditableText(task)
            }else{
                clickListener.onClick(task, 4)
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

    private fun toggleExpanded() {
        val titleParams = binding.taskItemTitle.layoutParams as  ViewGroup.MarginLayoutParams
        if (binding.expandedTaskNote.isVisible) {
            binding.expandedTaskNote.visibility = View.GONE
            binding.taskItemTitle.setMaxLines(1)
            titleParams.setMargins(titleParams.leftMargin, 0, titleParams.rightMargin, titleParams.bottomMargin)
        } else {
            binding.expandedTaskNote.visibility = View.VISIBLE
            binding.taskItemTitle.setMaxLines(4)
            titleParams.setMargins(titleParams.leftMargin, 12, titleParams.rightMargin, titleParams.bottomMargin)
            binding.taskItemTitle.layoutParams = titleParams
            val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.5f, 1f)
            val alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f)
            ObjectAnimator.ofPropertyValuesHolder(binding.expandedTaskNote, scaleY, alpha).apply {
                interpolator = AccelerateDecelerateInterpolator()
            }.start()
        }
    }

    private fun toggleEditableText(task: Task) {
        if (!binding.editTaskNoteText.isVisible) {
            binding.editTaskText.visibility = View.VISIBLE
            binding.editTaskText.setText(task.title)
            binding.editTaskNoteText.visibility = View.VISIBLE
            binding.editTaskNoteText.setText(task.description)
            binding.taskItemTitle.visibility = View.GONE
            binding.expandedTaskNote.visibility = View.GONE
            binding.deleteTaskBtn.visibility = View.VISIBLE
            binding.updateTaskBtn.visibility = View.VISIBLE
        } else {
            binding.taskItemTitle.visibility = View.VISIBLE
            binding.expandedTaskNote.visibility = View.VISIBLE
            binding.editTaskNoteText.visibility = View.GONE
            binding.editTaskText.visibility = View.GONE
            binding.deleteTaskBtn.visibility = View.GONE
            binding.updateTaskBtn.visibility = View.GONE
        }
    }

    fun getTaskColour(task: Task): Int {
        when (task.colour) {
            1 -> return Color.parseColor("#51C1E4")
            2 -> return Color.parseColor("#F2C94C")
            3 -> return Color.parseColor("#6FCF97")
            else -> return Color.parseColor("#F2C94C")
        }
    }
}