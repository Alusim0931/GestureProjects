package com.proyectosxml.gestureapp

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.MotionEvent


class FirstCanvasView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val paint: Paint = Paint()
    val bitmap = BitmapFactory.decodeResource(resources, R.drawable.campi)

    init {
        paint.color = Color.WHITE
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(bitmap, 100f, 10f, null)
    }
}

class CanvasMoveView(context: Context, private val view: View) {
    private var lastTouchX = 0f
    private var lastTouchY = 0f

    fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.rawX
        val y = event.rawY
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                lastTouchX = x
                lastTouchY = y
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = x - lastTouchX
                val deltaY = y - lastTouchY
                view.translationX += deltaX
                view.translationY += deltaY
                lastTouchX = x
                lastTouchY = y
            }
        }
        return true
    }
}
