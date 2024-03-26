package com.proyectosxml.gestureapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.view.ScaleGestureDetector



class MainActivity : AppCompatActivity() {
    private lateinit var moveView: MoveView
    private lateinit var rotationGestureDetector: RotationGestureDetector
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private var canInteractWithImage = false // Estado para controlar si se puede interactuar con la imagen
    private var imageState = ImageState()// Estado de la imagen
    private var isImageVisible = false
    private var isButton1Clicked = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val button1 = findViewById<ImageButton>(R.id.button1)
        val canvasView = findViewById<FirstCanvasView>(R.id.canvasView)

        // Inicializar la instancia de MoveView
        moveView = MoveView(this, canvasView)

        // Configurar el OnClickListener para el botón 1
        button1.setOnClickListener {
            if (!isButton1Clicked) {
                canInteractWithImage = true // Permitir interacción con la imagen al hacer clic en el botón 1
                canvasView.setOnTouchListener { _, event ->
                    moveView.onTouchEvent(event)
                    rotationGestureDetector.onTouchEvent(event)
                    scaleGestureDetector.onTouchEvent(event)
                    true
                }
                isButton1Clicked = true // Actualizar el estado a true después del primer clic
            }
        }

        // Configurar el OnClickListener para el botón 2
        val button2 = findViewById<ImageButton>(R.id.button2)
        button2.setOnClickListener {
            if (isImageVisible) {
                // Si la imagen está visible, ocúltala
                canvasView.visibility = View.GONE
            } else {
                // Si la imagen está oculta, muéstrala en una posición aleatoria
                val x = (0 until canvasView.width).random().toFloat()
                val y = (0 until canvasView.height).random().toFloat()
                canvasView.drawRandomSizedBitmapAt(x, y)
                canvasView.visibility = View.VISIBLE
            }
            // Alternar el estado de visibilidad de la imagen
            isImageVisible = !isImageVisible
        }

        // Inicializar las instancias de las clases
        rotationGestureDetector = RotationGestureDetector(object : OnRotationGestureListener {
            override fun onRotation(rotationDetector: RotationGestureDetector) {
                // Implementar la lógica de rotación aquí
                val angle = rotationDetector.getAngle()
                canvasView.rotation = angle
                // Actualizar el estado de la imagen al rotar en el FrameLayout
                imageState.rotation = angle
            }
        })

        scaleGestureDetector = ScaleGestureDetector(this, object : ScaleGestureDetector.OnScaleGestureListener {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                // Implementar la lógica de escalado aquí
                val scaleFactor = detector.scaleFactor
                canvasView.scaleX *= scaleFactor
                canvasView.scaleY *= scaleFactor
                return true
            }

            override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
                return true // Indicar que queremos que el gesto de escala continúe
            }

            override fun onScaleEnd(detector: ScaleGestureDetector) {
                // Aquí puedes realizar alguna acción al finalizar el gesto de escala si es necesario
            }
        })

    }
}