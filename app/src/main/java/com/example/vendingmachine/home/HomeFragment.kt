package com.example.vendingmachine.home

import android.os.Build
import android.os.Bundle
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.vendingmachine.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(){

    var balance = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding = FragmentHomeBinding.inflate(inflater)

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
                addToBalance()
                coins.visibility = View.VISIBLE
                true
            }
            else -> true
        }
    }

    fun addToBalance(){
        balance += 100
        balance_text.text = balance.toString()
    }
}