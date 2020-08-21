package com.example.vendingmachine.ui.tasks

import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.vendingmachine.data.models.Task
import com.example.vendingmachine.databinding.TaskItemBinding

class TasksAdapter(val viewModel: TasksViewModel) : ListAdapter<Task, TasksAdapter.TaskViewHolder>(TaskDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TaskItemBinding.inflate(layoutInflater, parent, false)
        binding.viewModel = viewModel
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task)
    }

    class TaskViewHolder(val binding: TaskItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task){
            binding.task = task
            binding.taskItemTitle.text = task.title
                binding.habitCount.visibility = View.GONE
                binding.habitCountSpacer.visibility = View.GONE
            binding.taskItemContainer.setCardBackgroundColor(getTaskColour(task))

            if(task.isCompleted) {
                binding.taskItemTitle.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                binding.taskItemCheckbox.isChecked = true
            }else if(!task.isCompleted){
                binding.taskItemCheckbox.isChecked = false
                binding.taskItemTitle.paintFlags = 0
            }
        }

        fun getTaskColour(task: Task) : Int{
            when(task.colour){
                1 -> return Color.parseColor("#51C1E4")
                2 -> return  Color.parseColor("#F2C94C")
                3-> return Color.parseColor("#6FCF97")
                else -> return Color.parseColor("#6FCF97")
            }
        }
    }

    class TaskDiffCallback() : DiffUtil.ItemCallback<Task>(){
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.equals(newItem)
        }
    }
}