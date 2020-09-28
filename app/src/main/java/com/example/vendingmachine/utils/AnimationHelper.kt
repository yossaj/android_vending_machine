package com.example.vendingmachine.utils

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.View
import android.view.animation.OvershootInterpolator
import com.example.vendingmachine.databinding.FragmentTasksBinding

object AnimationHelper {

    fun compressAnimation(view : View) : ObjectAnimator{

        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 0.0f)
        val alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 1f, 0.0f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(
            view,
            scaleY,
            alpha
        ).apply {
            interpolator = OvershootInterpolator()
        }
        return animator;
    }

    fun expandAnimation(view: View) {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0.5f, 1f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.5f, 1f)
        val alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f)
        ObjectAnimator.ofPropertyValuesHolder(view, scaleY, scaleX, alpha).apply {
            interpolator = OvershootInterpolator()
        }.start()
    }

    fun arrowGrow(view: View){
        val scaleXIncrease = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1.4f)
        val scaleYIncrease = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1.4f)

        ObjectAnimator.ofPropertyValuesHolder(view, scaleYIncrease, scaleXIncrease)
            .apply {
                interpolator = OvershootInterpolator()
            }.start()
    }

    fun arrowShrink(view: View){
        val scaleXDecrease = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.4f, 1f)
        val scaleYDecrease = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.4f, 1f)

        ObjectAnimator.ofPropertyValuesHolder(view, scaleYDecrease, scaleXDecrease)
            .apply {
                interpolator = OvershootInterpolator()
            }.start()
    }
    
}