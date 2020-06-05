package com.example.vendingmachine.tasks.taskdialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.vendingmachine.R
import com.example.vendingmachine.data.Task
import com.example.vendingmachine.data.TaskDatabase
import kotlinx.android.synthetic.main.dialog_add_task.*
import kotlinx.android.synthetic.main.view_task_dialog.*

class ViewTaskFragment : DialogFragment(){

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val application = requireNotNull(this.activity).application
        val datasource = TaskDatabase.getInstance(application)


        val viewmodel by lazy {
            val factory = AddTaskViewModelFactory(datasource)
            ViewModelProviders.of(this, factory).get<AddTaskViewModel>()
        }

        val dialog =  MaterialDialog(requireContext()).show {
            customView(R.layout.view_task_dialog)
            kanye_image.visibility = View.VISIBLE

        }

        return dialog
    }

}