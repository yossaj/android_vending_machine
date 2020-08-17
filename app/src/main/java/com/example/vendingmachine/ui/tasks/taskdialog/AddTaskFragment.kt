package com.example.vendingmachine.ui.tasks.taskdialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import androidx.navigation.fragment.navArgs
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.vendingmachine.R
import com.example.vendingmachine.data.Task
import com.example.vendingmachine.data.TaskDatabase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_add_task.*

@AndroidEntryPoint
class AddTaskFragment : DialogFragment(){

    val args : AddTaskFragmentArgs by navArgs()
    private val viewmodel : AddTaskViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val habitBool = args.habitCheck.equals(getString(R.string.habit))
        val trashBool = args.habitCheck.equals(getString(R.string.delete_all))


        if(habitBool){
            return addHabitDialog()
        }else if(trashBool){
            return deleteAllDialog()
        }else {
            return addTaskDialog()
        }
    }

    private fun addTaskDialog(): MaterialDialog {
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

    private fun deleteAllDialog(): MaterialDialog {
        val dialog = MaterialDialog(requireContext()).show {
            message(text = getString(R.string.delete_all_message))
            positiveButton(text = "Delete All") { dialog ->
                viewmodel.deleteAllTasks()

            }
            negativeButton(text = "Dismiss")
        }
        return dialog
    }

    private fun addHabitDialog(): MaterialDialog {
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
    }

}