package com.example.vendingmachine.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.vendingmachine.data.Task

class TasksAdapter : ListAdapter<Task, TasksAdapter.ViewHolder>(TaskDiffCallback()){





    class ViewHolder private constructor() :
        RecyclerView.ViewHolder() {

    }
}