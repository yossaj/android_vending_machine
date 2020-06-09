package com.example.vendingmachine.tasks

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.vendingmachine.data.Task
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
            if(task.habit){
                binding.habitCount.visibility = View.VISIBLE
                binding.habitCountSpacer.visibility = View.VISIBLE
                val habitCount = task.habitCount.toString()
                binding.habitCount.text = "0${habitCount}"
            }else if(!task.habit){
                binding.habitCount.visibility = View.GONE
                binding.habitCountSpacer.visibility = View.GONE
            }
            if(task.isCompleted) {
                binding.taskItemTitle.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                binding.taskItemCheckbox.isChecked = true
            }else if(!task.isCompleted){
                binding.taskItemCheckbox.isChecked = false
                binding.taskItemTitle.paintFlags = 0
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