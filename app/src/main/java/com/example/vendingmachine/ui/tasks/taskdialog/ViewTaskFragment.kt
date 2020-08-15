package com.example.vendingmachine.ui.tasks.taskdialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import androidx.navigation.fragment.navArgs
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.vendingmachine.R
import com.example.vendingmachine.data.TaskDatabase
import kotlinx.android.synthetic.main.view_task_dialog.*

class ViewTaskFragment : DialogFragment(){
    val viewId : ViewTaskFragmentArgs by navArgs()




    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val application = requireNotNull(this.activity).application
        val datasource = TaskDatabase.getInstance(application)


        val viewmodel by lazy {
            val factory = AddTaskViewModelFactory(datasource)
            ViewModelProviders.of(this, factory).get<AddTaskViewModel>()
        }

        val taskid = viewId.taskId

        viewmodel._currentTaskId.value = taskid
        viewmodel.getTaskToView()



        val dialog =  MaterialDialog(requireContext()).show {
            customView(R.layout.view_task_dialog)
            view_task_title.text = getString(R.string.loading)
            delete_item_button.setOnClickListener( View.OnClickListener {
                viewmodel.deleteTask()
                dismiss()
            })
        }

        viewmodel.requestedTask.observe(this, Observer {
            it?.let {
                dialog.view_task_title.text = it.title
                dialog.view_task_note.text = it.content
            }
        })

        return dialog
    }

}