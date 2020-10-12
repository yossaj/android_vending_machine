package com.venderino.vendingmachine.ui.home.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.venderino.vendingmachine.R
import com.venderino.vendingmachine.ui.home.HomeViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dialog_image.*
import kotlinx.android.synthetic.main.dialog_text.*

class HomeDialogFragment : DialogFragment() {
    private val viewmodel: HomeViewModel by activityViewModels<HomeViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val apikey = viewmodel.apiKey.value


        if (viewmodel.responseString.value == null) {
            viewmodel.getVendedApi()
        }


        var dialog = prepareCorrectDialog(apikey)

        viewmodel.responseString.observe(this, Observer {

            it?.let {
                if (apikey == "Cat" || apikey == "Dog") {
                    if(it != "You are currently offline") {
                        Picasso.get()
                            .load(it)
                            .placeholder(R.drawable.progress_animation)
                            .error(R.drawable.error_dog)
                            .into(dialog.dialog_image)
                    }else{
                        Picasso.get()
                            .load(R.drawable.error_dog)
                            .placeholder(R.drawable.progress_animation)
                            .error(R.drawable.error_dog)
                            .into(dialog.dialog_image)
                    }
                } else if (apikey != "Fox") {
                    dialog.dialog_text_view.text = it
                } else {
                    dialog.dialog_text_view.text = "Sorry, Currently Out Of Stock"
                }
            }
        })
        return dialog
    }

    fun prepareCorrectDialog(apikey: String?): MaterialDialog {

        when (apikey) {
            getString(R.string.mustache) ->
                return MaterialDialog(requireContext()).show {
                    customView(R.layout.dialog_text)
                    dialog_text_view.text = getString(R.string.loading)
                    dialog_title.text = getString(R.string.swanson_title)
                    text_dialog_icon.visibility = View.VISIBLE
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
            "Advice" ->
                return MaterialDialog(requireContext()).show {
                    customView(R.layout.dialog_text)
                    dialog_title.text = "Knockout Advice"
                }
            "Fox" -> {
                return MaterialDialog(requireContext()).show {
                    customView(R.layout.dialog_text)
                    dialog_title.text = "Foxy"
                }
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

    override fun onDestroy() {
        super.onDestroy()
    }


}