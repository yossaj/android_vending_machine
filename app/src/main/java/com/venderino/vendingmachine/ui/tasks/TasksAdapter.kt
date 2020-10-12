package com.venderino.vendingmachine.ui.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.venderino.vendingmachine.data.models.Task
import com.venderino.vendingmachine.databinding.TaskItemBinding

class TasksAdapter(val clickListener : TasksAdapter.OnClickListener) : ListAdapter<Task, TaskViewHolder>(TaskDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TaskItemBinding.inflate(layoutInflater, parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task, clickListener)

    }

    class TaskDiffCallback() : DiffUtil.ItemCallback<Task>(){
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            val bool = oldItem.equals(newItem)
            return bool
        }
    }

    class OnClickListener(val clickListener: (task : Task, viewType : Int) -> Unit) {
        fun onClick(task : Task, viewType: Int) = clickListener(task, viewType)
    }
}