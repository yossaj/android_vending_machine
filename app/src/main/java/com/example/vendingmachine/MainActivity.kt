package com.example.vendingmachine

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var balance = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        coin_drag_destination.setOnDragListener(dragListener)
        coins.setOnLongClickListener(){
            val dragShadowBuilder = View.DragShadowBuilder(it)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                it.startDragAndDrop(null, dragShadowBuilder, it, 0)
            }
            it.visibility =  View.INVISIBLE
            true
        }
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
