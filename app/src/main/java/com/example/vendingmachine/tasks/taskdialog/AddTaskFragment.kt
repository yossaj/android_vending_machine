package com.example.vendingmachine.tasks.taskdialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.vendingmachine.R
import com.example.vendingmachine.data.TaskDatabase
import com.example.vendingmachine.tasks.TaskViewModelFactory

class AddTaskFragment : DialogFragment(){

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val application = requireNotNull(this.activity).application
        val datasource = TaskDatabase.getInstance(application)

        val viewmodel by lazy {
            val factory = TaskViewModelFactory(datasource)
            ViewModelProviders.of(this, factory).get<AddTaskViewModel>()
        }

        val dialog =  MaterialDialog(requireContext()).show {
            customView(R.layout.dialog_add_task)
        }

        return dialog
    }

}