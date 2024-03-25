package com.proyectosxml.gestureapp

import android.content.Context
import android.view.MotionEvent
import android.view.View

class MoveView(context: Context, private val view: View) {
    private var dX = 0f
    private var dY = 0f

    fun onTouchEvent(event: MotionEvent) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // Guarda las coordenadas iniciales del toque
                dX = view.x - event.rawX
                dY = view.y - event.rawY
            }
            MotionEvent.ACTION_MOVE -> {
                // Actualiza la posición de la vista en respuesta al movimiento del toque
                view.animate()
                    .x(event.rawX + dX)
                    .y(event.rawY + dY)
                    .setDuration(0)
                    .start()
            }
        }
    }
}