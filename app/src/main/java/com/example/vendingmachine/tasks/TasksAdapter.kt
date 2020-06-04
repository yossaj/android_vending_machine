package com.example.vendingmachine.tasks

import android.graphics.Paint
import android.view.LayoutInflater
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
            if(task.isCompleted) {
                binding.taskItemTitle.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                binding.taskItemCheckbox.isChecked = true
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