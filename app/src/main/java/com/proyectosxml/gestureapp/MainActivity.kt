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
    private lateinit var moveCanvasView: MoveView
    private lateinit var moveFrameLayout: MoveView
    private lateinit var rotationGestureDetector: RotationGestureDetector
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private var canInteractWithImage = false // Estado para controlar si se puede interactuar con la imagen

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val button1 = findViewById<ImageButton>(R.id.button1)
        val frameLayout = findViewById<FrameLayout>(R.id.frameLayout)
        val fullScreenImage = findViewById<ImageView>(R.id.fullScreenImage)
        val canvasView = findViewById<FirstCanvasView>(R.id.canvasView)

        // Crear una variable para la imagen del canvas
        val canvasImage: Bitmap = canvasView.bitmap

        // Configurar el OnClickListener para el botón 1
        button1.setOnClickListener {
            canInteractWithImage = true // Permitir interacción con la imagen al hacer clic en el botón 1
        }

        // Inicializar las instancias de las clases
        moveView = MoveView(this, fullScreenImage)
        moveCanvasView = MoveView(this, canvasView)
        moveFrameLayout = MoveView(this, frameLayout)
        rotationGestureDetector = RotationGestureDetector(object : OnRotationGestureListener {
            override fun onRotation(rotationDetector: RotationGestureDetector) {
                // Implementar la lógica de rotación aquí
                val angle = rotationDetector.getAngle()
                fullScreenImage.rotation = angle
            }
        })
        scaleGestureDetector = ScaleGestureDetector(this, object : ScaleGestureDetector.OnScaleGestureListener {
            override fun onScale(scaleFactor: Float) {
                TODO("Not yet implemented")
            }

            override fun onScale(detector: android.view.ScaleGestureDetector): Boolean {
                // Implementar la lógica de escalado aquí
                val scaleFactor = detector.scaleFactor
                fullScreenImage.scaleX *= scaleFactor
                fullScreenImage.scaleY *= scaleFactor
                return true
            }

            override fun onScaleBegin(detector: android.view.ScaleGestureDetector): Boolean {
                return true
            }

            override fun onScaleEnd(detector: android.view.ScaleGestureDetector) {
            }
        })

        frameLayout.setOnTouchListener { _, event ->
            if (canInteractWithImage) {
                moveView.onTouchEvent(event)
                rotationGestureDetector.onTouchEvent(event)
                scaleGestureDetector.onTouchEvent(event)
            } else {
                moveFrameLayout.onTouchEvent(event)
            }
            true
        }

        canvasView.setOnTouchListener { _, event ->
            moveCanvasView.onTouchEvent(event)
            if (event.action == MotionEvent.ACTION_UP) {
                if (canInteractWithImage) {
                    val canvasImage: Bitmap = canvasView.bitmap
                    fullScreenImage.setImageBitmap(canvasImage)
                    frameLayout.visibility = View.VISIBLE
                    canvasView.visibility = View.GONE
                }
            }
            true
        }

        // Configurar el OnClickListener para el botón de regreso
        val backToCanvasButton = findViewById<ImageButton>(R.id.backToCanvasButton)
        backToCanvasButton.setOnClickListener {
            frameLayout.visibility = View.GONE
            canvasView.visibility = View.VISIBLE
            canInteractWithImage = false // Restablecer el estado para deshabilitar la interacción con la imagen
        }
    }
}