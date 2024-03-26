package com.proyectosxml.gestureapp

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.view.GestureDetector
import android.view.MotionEvent


class MainActivity : AppCompatActivity() {
    private lateinit var moveView: MoveView
    private lateinit var moveCanvasView: CanvasMoveView
    private lateinit var moveFrameLayout: MoveView
    private lateinit var rotationGestureDetector: RotationGestureDetector
    private lateinit var scaleGestureDetector: CustomScaleGestureDetector
    private var canInteractWithImage = false // Estado para controlar si se puede interactuar con la imagen
    private var imageState = ImageState() // Estado de la imagen
    private lateinit var fullScreenImage: ImageView

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val button1 = findViewById<ImageButton>(R.id.button1)
        val frameLayout = findViewById<FrameLayout>(R.id.frameLayout)
        fullScreenImage = findViewById<ImageView>(R.id.fullScreenImage)
        val canvasView = findViewById<FirstCanvasView>(R.id.canvasView)

        // Inicializar la instancia de CanvasMoveView
        moveCanvasView = CanvasMoveView(this, canvasView)

        // Configurar el OnClickListener para el botón 1
        button1.setOnClickListener {
            canInteractWithImage = true // Permitir interacción con la imagen al hacer clic en el botón 1
            fullScreenImage.setOnTouchListener { _, event ->
                scaleGestureDetector.onTouchEvent(event)
                rotationGestureDetector.onTouchEvent(event)
                moveView.onTouchEvent(event)
                true
            }
        }

        // Configurar el OnClickListener para el botón 2
        val button2 = findViewById<ImageButton>(R.id.button2)
        button2.setOnClickListener {
            canInteractWithImage = false
            val x = (0 until canvasView.width).random().toFloat()
            val y = (0 until canvasView.height).random().toFloat()
            canvasView.drawRandomSizedBitmapAt(x, y) // Mostrar la imagen cuando se haga clic en el botón 2
        }

        // Inicializar las instancias de las clases
        moveView = MoveView(this, fullScreenImage)
        moveFrameLayout = MoveView(this, frameLayout)
        rotationGestureDetector = RotationGestureDetector(object : OnRotationGestureListener {
            override fun onRotation(rotationDetector: RotationGestureDetector) {
                // Implementar la lógica de rotación aquí
                val angle = rotationDetector.getAngle()
                fullScreenImage.rotation = angle
                // Actualizar el estado de la imagen al rotar en el FrameLayout
                imageState.rotation = angle
            }
        })

        scaleGestureDetector = CustomScaleGestureDetector(this, object : CustomScaleGestureDetector.OnScaleGestureListener {
            override fun onScale(scaleFactor: Float) {
                // Implementar la lógica de escalado aquí
                fullScreenImage.scaleX *= scaleFactor
                fullScreenImage.scaleY *= scaleFactor
            }
        })

        frameLayout.setOnTouchListener { _, event ->
            if (canInteractWithImage) {
                rotationGestureDetector.onTouchEvent(event)
                moveView.onTouchEvent(event)
            } else {
                moveFrameLayout.onTouchEvent(event)
            }
            true
        }

        canvasView.setOnTouchListener { _, event ->
            if (canInteractWithImage) {
                moveCanvasView.onTouchEvent(event)
            }
            true
        }

        // Configurar el OnClickListener para el botón de regreso
        val backToCanvasButton = findViewById<ImageButton>(R.id.backToCanvasButton)
        backToCanvasButton.setOnClickListener {
            frameLayout.visibility = View.GONE
            canvasView.visibility = View.VISIBLE
            canInteractWithImage = false // Restablecer el estado para deshabilitar la interacción con la imagen

            // Restaurar el estado de la imagen
            fullScreenImage.translationX = imageState.x
            fullScreenImage.translationY = imageState.y
            fullScreenImage.rotation = imageState.rotation
            fullScreenImage.scaleX = imageState.scaleX
            fullScreenImage.scaleY = imageState.scaleY
        }
    }

    private fun getPositionOfImageOnScreen(imageView: ImageView): Pair<Float, Float> {
        val location = IntArray(2)
        imageView.getLocationOnScreen(location)
        val x = location[0].toFloat()
        val y = location[1].toFloat()
        return Pair(x, y)
    }
}