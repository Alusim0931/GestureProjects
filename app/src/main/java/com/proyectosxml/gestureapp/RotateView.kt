package com.proyectosxml.gestureapp

import android.view.MotionEvent

interface OnRotationGestureListener {
    fun onRotation(rotationDetector: RotationGestureDetector)
}

class RotationGestureDetector(private val mListener: OnRotationGestureListener) {
    private var mPrevX: Float = 0.toFloat()
    private var mPrevY: Float = 0.toFloat()
    private var mPtrID1: Int = INVALID_POINTER_ID
    private var mPtrID2: Int = INVALID_POINTER_ID
    private var mAngle: Float = 0.toFloat()

    fun getAngle(): Float {
        return mAngle
    }

    fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                mPtrID1 = event.getPointerId(event.actionIndex)
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                mPtrID2 = event.getPointerId(event.actionIndex)
                mPrevX = event.getX(event.findPointerIndex(mPtrID1))
                mPrevY = event.getY(event.findPointerIndex(mPtrID1))
            }
            MotionEvent.ACTION_MOVE -> {
                val idx1 = event.findPointerIndex(mPtrID1)
                val idx2 = event.findPointerIndex(mPtrID2)
                if (idx1 != -1 && idx2 != -1) {
                    val currX = event.getX(idx1)
                    val currY = event.getY(idx1)
                    mAngle = angleBetweenLines(mPrevX, mPrevY, currX, currY)
                    if (mListener != null) {
                        mListener.onRotation(this)
                    }
                }
            }
            MotionEvent.ACTION_UP -> mPtrID1 = INVALID_POINTER_ID
            MotionEvent.ACTION_POINTER_UP -> mPtrID2 = INVALID_POINTER_ID
            MotionEvent.ACTION_CANCEL -> {
                mPtrID1 = INVALID_POINTER_ID
                mPtrID2 = INVALID_POINTER_ID
            }
        }
        return true
    }

    private fun angleBetweenLines(fX: Float, fY: Float, sX: Float, sY: Float): Float {
        return ((Math.atan2((sY - fY).toDouble(), (sX - fX).toDouble()) * 180 / Math.PI).toFloat())
    }

    companion object {
        private val INVALID_POINTER_ID = -1
    }
}
