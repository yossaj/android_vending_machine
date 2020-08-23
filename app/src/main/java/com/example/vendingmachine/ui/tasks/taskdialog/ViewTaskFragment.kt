package com.example.vendingmachine.ui.tasks.taskdialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.vendingmachine.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.view_task_dialog.*

@AndroidEntryPoint
class ViewTaskFragment : DialogFragment(){
    private val viewmodel : AddTaskViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog =  MaterialDialog(requireContext()).show {
            customView(R.layout.view_task_dialog)
            view_task_title.text = getString(R.string.loading)
            delete_item_button.setOnClickListener( View.OnClickListener {
                viewmodel.deleteTask()
                dismiss()
            })
        }

        viewmodel.currentTask.observe(this, Observer {
            it?.let {
                dialog.view_task_title.text = it.title
                dialog.view_task_note.text = it.description
            }
        })

        return dialog
    }
}