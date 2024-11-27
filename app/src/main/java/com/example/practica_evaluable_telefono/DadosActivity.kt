package com.example.practica_evaluable_telefono

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.example.practica_evaluable_telefono.databinding.ActivityMainDadosBinding
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class DadosActivity : AppCompatActivity() {

    private lateinit var bindingMain: ActivityMainDadosBinding
    private var resultado: Int = 0
    private var modoJuego: String = "SUMA"
    private var apuesta: Int = 0
    private var numDados: Int = 3
    private var tiempoEntreTiradas: Long = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingMain = ActivityMainDadosBinding.inflate(layoutInflater)
        setContentView(bindingMain.root)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.gris)

        val tiempoString = intent.getStringExtra("TIME_BETWEEN_ROLLS") ?: "1 seg"
        tiempoEntreTiradas = tiempoString.split(" ")[0].toLong() * 1000
        numDados = intent.getIntExtra("NUM_DADOS", 3)
        apuesta = intent.getIntExtra("APUESTA", 0)
        modoJuego = intent.getStringExtra("MODO_JUEGO") ?: "SUMA"

        initEvent()
    }

    private fun initEvent() {
        bindingMain.txtResultado.visibility = View.INVISIBLE
        bindingMain.imageButton.setOnClickListener {
            // Mostrar solo los dados seleccionados
            showSelectedDice()

            bindingMain.txtResultado.visibility = View.VISIBLE
            resultado = 0
            animateCup() // Animación del vaso
            game()
        }
    }

    private fun showSelectedDice() {
        // Configurar visibilidad de dados según numDados
        bindingMain.imagviewDado1.visibility = if (numDados >= 1) View.VISIBLE else View.GONE
        bindingMain.imagviewDado2.visibility = if (numDados >= 2) View.VISIBLE else View.GONE
        bindingMain.imagviewDado3.visibility = if (numDados == 3) View.VISIBLE else View.GONE
    }


    private fun game() {
        scheduleRun()
    }

    private fun scheduleRun() {
        val schedulerExecutor = Executors.newSingleThreadScheduledExecutor()

        for (i in 1..numDados) {
            schedulerExecutor.schedule(
                {
                    throwDadoInTime()
                },
                tiempoEntreTiradas * i, TimeUnit.MILLISECONDS
            )
        }

        schedulerExecutor.schedule(
            {
                viewResult()
            },
            tiempoEntreTiradas * (numDados + 1), TimeUnit.MILLISECONDS
        )

        schedulerExecutor.shutdown()
    }

    private fun throwDadoInTime() {
        // Array de ImageViews
        val imagViews: Array<ImageView> = arrayOf(
            bindingMain.imagviewDado1,
            bindingMain.imagviewDado2,
            bindingMain.imagviewDado3
        )

        // Generar números aleatorios para cada dado
        val valoresDados = IntArray(numDados) { Random.nextInt(1, 7) }

        // Calcular el resultado basado en el modo de juego
        resultado = if (modoJuego == "SUMA") {
            valoresDados.sum()
        } else if (modoJuego == "MULTIPLICADOR") {
            valoresDados.fold(1) { acc, num -> acc * num }
        } else {
            0 // Valor por defecto en caso de modo desconocido
        }

        // Actualizar UI
        runOnUiThread {
            for (i in 0 until numDados) {
                animateDice(imagViews[i]) // Animar el dado correspondiente
                selectView(imagViews[i], valoresDados[i]) // Asignar imagen según valor
            }
        }
    }



    private fun selectView(imgV: ImageView, v: Int) {
        when (v) {
            1 -> imgV.setImageResource(R.drawable.dado1)
            2 -> imgV.setImageResource(R.drawable.dado2)
            3 -> imgV.setImageResource(R.drawable.dado3)
            4 -> imgV.setImageResource(R.drawable.dado4)
            5 -> imgV.setImageResource(R.drawable.dado5)
            6 -> imgV.setImageResource(R.drawable.dado6)
        }
    }

    private fun viewResult() {
        val resultadoFinal = resultado
        val mensajeResultado = if (resultadoFinal == apuesta) {
            "¡Ganaste! Resultado: $resultadoFinal (Tu apuesta: $apuesta)"
        } else {
            "Perdiste. Resultado: $resultadoFinal (Tu apuesta: $apuesta)"
        }

        runOnUiThread {
            bindingMain.txtResultado.text = resultadoFinal.toString() // Mostrar suma total en txtResultado
            bindingMain.txtMensajeJuego.text = mensajeResultado
        }
    }


    private fun animateCup() {
        val rotateAnimator = ObjectAnimator.ofFloat(bindingMain.imageButton, "rotation", 0f, 360f)
        val scaleXAnimator = ObjectAnimator.ofFloat(bindingMain.imageButton, "scaleX", 1f, 1.2f, 1f)
        val scaleYAnimator = ObjectAnimator.ofFloat(bindingMain.imageButton, "scaleY", 1f, 1.2f, 1f)

        rotateAnimator.duration = 1000
        scaleXAnimator.duration = 1000
        scaleYAnimator.duration = 1000

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(rotateAnimator, scaleXAnimator, scaleYAnimator)
        animatorSet.start()
    }

    private fun animateDice(diceView: ImageView) {
        val translateYAnimator = ObjectAnimator.ofFloat(diceView, "translationY", 0f, -50f, 0f)
        val scaleXAnimator = ObjectAnimator.ofFloat(diceView, "scaleX", 1f, 1.3f, 1f)
        val scaleYAnimator = ObjectAnimator.ofFloat(diceView, "scaleY", 1f, 1.3f, 1f)

        translateYAnimator.duration = 500
        scaleXAnimator.duration = 500
        scaleYAnimator.duration = 500

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(translateYAnimator, scaleXAnimator, scaleYAnimator)
        animatorSet.start()
    }
}
