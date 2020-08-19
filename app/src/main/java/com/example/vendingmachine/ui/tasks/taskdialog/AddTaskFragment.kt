package com.example.vendingmachine.ui.tasks.taskdialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.vendingmachine.R
import com.example.vendingmachine.data.models.Task
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_add_task.*

@AndroidEntryPoint
class AddTaskFragment : DialogFragment(){

    val args : AddTaskFragmentArgs by navArgs()
    private val viewmodel : AddTaskViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val trashBool = args.habitCheck.equals(getString(R.string.delete_all))

       if(trashBool){
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
                val currentTask =
                    Task(title, note, 1, 2)
                viewmodel.setCurrentTask(currentTask)
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

}