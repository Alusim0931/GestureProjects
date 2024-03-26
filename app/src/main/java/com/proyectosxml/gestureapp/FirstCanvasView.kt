package com.proyectosxml.gestureapp

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.MotionEvent


class FirstCanvasView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val paint: Paint = Paint()
    private var bitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.campi)
    private var bitmapX = 100f
    private var bitmapY = 10f

    init {
        paint.color = Color.WHITE
        paint.alpha = 0 // Inicializar la imagen como transparente
    }

    fun hideBitmap() {
        paint.alpha = 0 // Hacer la imagen transparente
        invalidate() // Redraw the view
    }

    fun drawBitmapAt(x: Float, y: Float) {
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        val screenHeight = Resources.getSystem().displayMetrics.heightPixels

        // Calcular los límites para las coordenadas x e y de la imagen
        val maxX = screenWidth - bitmap.width
        val maxY = screenHeight - bitmap.height

        // Asegurar que las coordenadas x e y estén dentro de los límites de la pantalla
        val clampedX = x.coerceIn(0f, maxX.toFloat())
        val clampedY = y.coerceIn(0f, maxY.toFloat())

        // Actualizar las coordenadas de la imagen
        bitmapX = clampedX
        bitmapY = clampedY

        paint.alpha = 255 // Hacer la imagen opaca
        invalidate() // Redibujar la vista
    }

    fun drawRandomSizedBitmapAt(x: Float, y: Float) {
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        val screenHeight = Resources.getSystem().displayMetrics.heightPixels

        // Obtener el ancho y alto máximo para la imagen
        val maxWidth = screenWidth * 0.8f // Limitar el ancho al 80% del ancho de la pantalla
        val maxHeight = screenHeight * 0.8f // Limitar el alto al 80% del alto de la pantalla

        // Generar tamaños aleatorios dentro de los límites máximos
        val randomWidth = (100..maxWidth.toInt()).random().toFloat() // Suponiendo un tamaño mínimo de 100
        val randomHeight = (100..maxHeight.toInt()).random().toFloat() // Suponiendo un tamaño mínimo de 100

        // Escalar la imagen al tamaño aleatorio generado
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, randomWidth.toInt(), randomHeight.toInt(), true)

        // Calcular los límites para las coordenadas x e y de la imagen
        val maxX = screenWidth - scaledBitmap.width
        val maxY = screenHeight - scaledBitmap.height

        // Asegurar que las coordenadas x e y estén dentro de los límites de la pantalla
        val clampedX = x.coerceIn(0f, maxX.toFloat())
        val clampedY = y.coerceIn(0f, maxY.toFloat())

        // Actualizar las coordenadas de la imagen y el bitmap
        bitmapX = clampedX
        bitmapY = clampedY
        bitmap = scaledBitmap // Actualizar el bitmap con el bitmap escalado

        paint.alpha = 255 // Hacer la imagen opaca
        invalidate() // Redibujar la vista
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(bitmap, bitmapX, bitmapY, paint)
    }
}

class CanvasMoveView(context: Context, private val view: View) {
    private var lastTouchX = 0f
    private var lastTouchY = 0f
    private val screenWidth = Resources.getSystem().displayMetrics.widthPixels
    private val screenHeight = Resources.getSystem().displayMetrics.heightPixels

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
                val newTranslationX = view.translationX + deltaX
                val newTranslationY = view.translationY + deltaY

                // Verificar que la nueva posición esté dentro de los límites de la pantalla
                if (newTranslationX >= 0 && newTranslationX <= screenWidth - view.width) {
                    view.translationX = newTranslationX
                }
                if (newTranslationY >= 0 && newTranslationY <= screenHeight - view.height) {
                    view.translationY = newTranslationY
                }

                lastTouchX = x
                lastTouchY = y
            }
        }
        return true
    }
}