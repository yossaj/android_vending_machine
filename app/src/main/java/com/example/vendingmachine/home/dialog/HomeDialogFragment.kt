package com.example.vendingmachine.home.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.input.input
import com.example.vendingmachine.R
import kotlinx.android.synthetic.main.dialog_text.*
import kotlinx.android.synthetic.main.fragment_home.*

class HomeDialogFragment : DialogFragment(){


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = MaterialDialog(requireContext()).show{
            customView(R.layout.dialog_text)
            dialog_text_view.text = "This is editable text"
        }

//        return super.onCreateDialog(savedInstanceState)

        return  dialog
    }
}