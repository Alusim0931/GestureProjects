package com.proyectosxml.gestureapp

import android.content.Context
import android.view.MotionEvent

class CustomScaleGestureDetector(context: Context, private val listener: OnScaleGestureListener) :
    ScaleGestureDetector(context, listener) {

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)

        when (event.actionMasked) {
            MotionEvent.ACTION_MOVE -> {
                val scaleFactor = this.scaleFactor
                listener.onScale(scaleFactor)
            }
        }

        return true
    }

    interface OnScaleGestureListener : android.view.ScaleGestureDetector.OnScaleGestureListener {
        fun onScale(scaleFactor: Float)
        override fun onScale(detector: android.view.ScaleGestureDetector): Boolean {
            TODO("Not yet implemented")
        }

        override fun onScaleBegin(detector: android.view.ScaleGestureDetector): Boolean {
            TODO("Not yet implemented")
        }

        override fun onScaleEnd(detector: android.view.ScaleGestureDetector) {
            TODO("Not yet implemented")
        }
    }
}