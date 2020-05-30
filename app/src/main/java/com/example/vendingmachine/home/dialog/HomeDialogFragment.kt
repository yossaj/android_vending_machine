package com.example.vendingmachine.home.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.vendingmachine.R
import kotlinx.android.synthetic.main.dialog_text.*
import kotlinx.android.synthetic.main.fragment_home.*

class HomeDialogFragment : DialogFragment(){
    val args : HomeDialogFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = MaterialDialog(requireContext()).show{
            customView(R.layout.dialog_text)
            dialog_text_view.text = args.apikey
        }

//        return super.onCreateDialog(savedInstanceState)

        return  dialog
    }
}