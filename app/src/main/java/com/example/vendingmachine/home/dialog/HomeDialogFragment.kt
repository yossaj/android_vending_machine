package com.example.vendingmachine.home.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import androidx.navigation.fragment.navArgs
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.vendingmachine.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dialog_image.*
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
                if(args.apikey == "Cat" || args.apikey == "Dog") {
                    Picasso.get()
                        .load(it)
                        .placeholder(R.drawable.progress_animation)
                        .error(R.drawable.error_dog)
                        .into(dialog.dialog_image)

                }else{
                    dialog.dialog_text_view.text = it
                }

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
            getString(R.string.cat) ->
                return MaterialDialog(requireContext()).show {
                    customView(R.layout.dialog_image)
                    dialog_title_image.text = "Little Fur Balls"
                }
            getString(R.string.dog) ->
                return MaterialDialog(requireContext()).show {
                    customView(R.layout.dialog_image)
                    dialog_title_image.text = "Daily Dose of Doggo"
                }

            else ->
                return MaterialDialog(requireContext()).show {
                    customView(R.layout.dialog_text)
                    dialog_text_view.text = getString(R.string.loading)
                }

        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        dialog.dismiss()
        super.onDismiss(dialog)
    }


}