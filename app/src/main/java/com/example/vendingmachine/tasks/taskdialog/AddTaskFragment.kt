package com.example.vendingmachine.tasks.taskdialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import androidx.navigation.fragment.navArgs
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.vendingmachine.R
import com.example.vendingmachine.data.Task
import com.example.vendingmachine.data.TaskDatabase
import kotlinx.android.synthetic.main.dialog_add_task.*

class AddTaskFragment : DialogFragment(){

    val args : AddTaskFragmentArgs by navArgs()


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val application = requireNotNull(this.activity).application
        val datasource = TaskDatabase.getInstance(application)


        val viewmodel by lazy {
            val factory = AddTaskViewModelFactory(datasource)
            ViewModelProviders.of(this, factory).get<AddTaskViewModel>()
        }
        val habitBool = args.habitCheck.equals(getString(R.string.habit))

        if(habitBool){
            val dialog = MaterialDialog(requireContext()).show {
                customView(R.layout.dialog_add_task)
                title_text_layout.hint = "Title of Habit"
                note_text_layout.hint = "Notes on Habit"

                positiveButton {
                    val title: String = title_text.editableText.toString()
                    val note: String = note_text.editableText.toString()
                    val currentTask = Task(title, note, true)
                    viewmodel._currentNewTask.value = currentTask
                    viewmodel.addTask()
                }
            }
            return dialog

        }else {
            val dialog = MaterialDialog(requireContext()).show {
                customView(R.layout.dialog_add_task)
                title_text_layout.hint = "Title of Task"
                note_text_layout.hint = "Notes on Task"

                positiveButton {
                    val title: String = title_text.editableText.toString()
                    val note: String = note_text.editableText.toString()
                    val currentTask = Task(title, note)
                    viewmodel._currentNewTask.value = currentTask
                    viewmodel.addTask()
                }
            }
            return dialog
        }
    }

}