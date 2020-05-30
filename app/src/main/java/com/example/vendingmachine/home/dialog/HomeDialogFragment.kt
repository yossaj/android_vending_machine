package com.example.vendingmachine.home.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import androidx.navigation.fragment.navArgs
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.vendingmachine.R
import kotlinx.android.synthetic.main.dialog_text.*

class HomeDialogFragment : DialogFragment(){
    val args : HomeDialogFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val viewmodel by lazy {
            val factory =
                HomeDialogViewModelFactory(args.apikey)
            ViewModelProviders.of(this, factory).get<HomeDialogViewModel>()
        }

        if(viewmodel.responseString.value == null) {
            viewmodel.getVendedApi(requireContext())
        }

        val dialog = prepareCorrectDialog()

        viewmodel.responseString.observe(this, Observer {
            it?.let{
                dialog.dialog_text_view.text = it

            }
        })

        return  dialog
    }

    fun prepareCorrectDialog() : MaterialDialog {

        when (args.apikey) {
            getString(R.string.mustache) ->
            return MaterialDialog(requireContext()).show {
                customView(R.layout.dialog_text)
                dialog_text_view.text = getString(R.string.loading)
                dialog_title.text = getString(R.string.swanson_title)
            }
            getString(R.string.bull) ->
                return MaterialDialog(requireContext()).show {
                    customView(R.layout.dialog_text)
                    dialog_text_view.text = getString(R.string.loading)
                    dialog_title.text = getString(R.string.bull_title)
                }
            else ->
                return MaterialDialog(requireContext()).show {
                    customView(R.layout.dialog_text)
                    dialog_text_view.text = getString(R.string.loading)
                }

        }
    }


}