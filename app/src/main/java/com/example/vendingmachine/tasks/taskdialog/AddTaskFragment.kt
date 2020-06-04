package com.example.vendingmachine.tasks.taskdialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.vendingmachine.R
import com.example.vendingmachine.data.Task
import com.example.vendingmachine.data.TaskDatabase
import com.example.vendingmachine.databinding.DialogAddTaskBinding
import com.example.vendingmachine.tasks.TaskViewModelFactory
import kotlinx.android.synthetic.main.dialog_add_task.*

class AddTaskFragment : DialogFragment(){



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val application = requireNotNull(this.activity).application
        val datasource = TaskDatabase.getInstance(application)


        val viewmodel by lazy {
            val factory = AddTaskViewModelFactory(datasource)
            ViewModelProviders.of(this, factory).get<AddTaskViewModel>()
        }

        val dialog =  MaterialDialog(requireContext()).show {
            customView(R.layout.dialog_add_task)


            positiveButton {
                val title : String = title_text.editableText.toString()
                val note : String = note_text.editableText.toString()
                val currentTask = Task(title, note)
                viewmodel._currentTask.value = currentTask
                viewmodel.addTask()
            }

        }

        return dialog
    }

}