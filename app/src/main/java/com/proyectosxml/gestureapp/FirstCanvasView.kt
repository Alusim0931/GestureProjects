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
    var bitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.campi)
    var bitmapX = 100f
    var bitmapY = 10f
    val screenWidth = Resources.getSystem().displayMetrics.widthPixels
    val screenHeight = Resources.getSystem().displayMetrics.heightPixels

    init {
        paint.color = Color.WHITE
        paint.alpha = 0 // Inicializar la imagen como transparente
    }

    fun hideBitmap() {
        paint.alpha = 0 // Hacer la imagen transparente
        invalidate() // Redraw the view
    }

    fun drawBitmapAt(x: Float, y: Float) {
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

        // Asegurar que las coordenadas x e y estén dentro de los límites de la pantalla
        val maxX = (screenWidth - bitmap.width).coerceAtLeast(0)
        val maxY = (screenHeight - bitmap.height).coerceAtLeast(0)
        val clampedX = bitmapX.coerceIn(0f, maxX.toFloat())
        val clampedY = bitmapY.coerceIn(0f, maxY.toFloat())

        canvas.drawBitmap(bitmap, clampedX, clampedY, paint)
    }
}

class CanvasMoveView(context: Context, private val canvasView: FirstCanvasView, private val bottomBarHeight: Int) {
    private var dX = 0f
    private var dY = 0f
    private val screenWidth = canvasView.screenWidth
    private val screenHeight = canvasView.screenHeight - bottomBarHeight

    fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                dX = canvasView.bitmapX - event.rawX
                dY = canvasView.bitmapY - event.rawY
            }

            MotionEvent.ACTION_MOVE -> {
                val newX = event.rawX + dX
                val newY = event.rawY + dY

                // Asegurar que la vista no sea más grande que la pantalla
                val viewWidth = canvasView.bitmap.width.coerceAtMost(screenWidth)
                val viewHeight = canvasView.bitmap.height.coerceAtMost(screenHeight)

                // Calcular los límites para las coordenadas x e y
                val minX = 0f
                val maxX = screenWidth - viewWidth.toFloat()
                val minY = 0f
                val maxY = screenHeight - viewHeight.toFloat()

                // Asegurar que las coordenadas x e y estén dentro de los límites de la pantalla
                val clampedX = newX.coerceIn(minX, maxX)
                val clampedY = newY.coerceIn(minY, maxY)

                // Aplicar la nueva posición a la vista
                canvasView.bitmapX = clampedX
                canvasView.bitmapY = clampedY
                canvasView.invalidate() // Redibujar la vista
            }
        }
        return true
    }
}