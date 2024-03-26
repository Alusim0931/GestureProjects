package com.proyectosxml.gestureapp

import android.content.Context
import android.view.MotionEvent
import android.view.ScaleGestureDetector

class ScaleGestureDetector(context: Context, private val listener: OnScaleGestureListener) : ScaleGestureDetector(context, listener) {
    private var scaleFactor = 1.0f

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                scaleFactor = 1.0f
            }
            MotionEvent.ACTION_MOVE -> {
                scaleFactor *= scaleFactor
            }
        }

        return true
    }

    interface OnScaleGestureListener : ScaleGestureDetector.OnScaleGestureListener {
        fun onScale(scaleFactor: Float)
    }
}