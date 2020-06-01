package com.example.vendingmachine.home

import android.os.Build
import android.os.Bundle
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.vendingmachine.R
import com.example.vendingmachine.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(){

    lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding = FragmentHomeBinding.inflate(inflater)

        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        binding.homeViewModel = viewModel
        binding.setLifecycleOwner(this)
        val navController = findNavController()

        viewModel.apiKey.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (viewModel.balance >= 100) {

                    navController.navigate(
                        HomeFragmentDirections.actionHomeFragmentToHomeDialogFragment(
                            it
                        )
                    )
                    viewModel.resetAPIKey()
                    viewModel.updateDisplayedBalanceUponSale()


                } else {
                    Snackbar.make(binding.root, "Insufficient Funds", Snackbar.LENGTH_LONG)
                        .setActionTextColor(resources.getColor(R.color.colorPrimaryDark))
                        .setTextColor(resources.getColor(R.color.colorPrimaryDark))
                        .setBackgroundTint(resources.getColor(R.color.colorAccent))
                        .show()
                }
            }
        })


        binding.coinDragDestination.setOnDragListener(dragListener)
        binding.coins.setOnLongClickListener(){
            val dragShadowBuilder = View.DragShadowBuilder(it)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                it.startDragAndDrop(null, dragShadowBuilder, it, 0)
            }
            it.visibility =  View.INVISIBLE
            true
        }

        return binding.root
    }



    val dragListener = View.OnDragListener{view, event ->
        when(event.action){
            DragEvent.ACTION_DRAG_ENTERED -> {
                view.invalidate()
                true
            }
            DragEvent.ACTION_DRAG_LOCATION -> true
            DragEvent.ACTION_DRAG_EXITED -> {
                view.invalidate()
                true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                view.invalidate()
                true
            }
            DragEvent.ACTION_DRAG_STARTED -> true
            DragEvent.ACTION_DROP -> {
                viewModel.addToBalanceAndConvertToString()
                coins.visibility = View.VISIBLE
                true
            }
            else -> true
        }
    }



}