package com.example.practica_evaluable_telefono

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.example.practica_evaluable_telefono.databinding.ActivityChistesBinding
import java.util.Locale
import kotlin.random.Random

class ChistesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChistesBinding
    private lateinit var textToSpeech: TextToSpeech
    private val TOUCH_MAX_TIME = 500 // en milisegundos
    private var touchLastTime: Long = 0
    private lateinit var handler: Handler
    val MYTAG = "LOGCAT"

    // Lista de chistes
    private val chistes = listOf(
        "Esto era uno que era tan pequeño, tan pequeño, que cuando nacía un diente le salía una muela.",
        "¿Qué hace una abeja en el gimnasio? ¡Zum-ba!",
        "¿Por qué los pájaros no usan WhatsApp? Porque ya tienen Twitter.",
        "¿Qué le dice una iguana a su hermana gemela? ¡Iguaná tú!",
        "¿Cómo se despiden los químicos? Ácido un placer.",
        "¿Qué le dice un jardinero a otro? ¡Disfrutemos mientras podamos!",
        "¿Qué hace una oreja en un jardín? Escuchando crecer las plantas.",
        "¿Por qué los esqueletos no pelean entre ellos? Porque no tienen el valor.",
        "¿Qué hace una morsa en el gimnasio? ¡Usar su fuerza de colmillo!",
        "¿Qué le dice un semáforo a otro? No me mires que me pongo rojo."
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChistesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.gris)
        configureTextToSpeech()
        initHandler()
        initEvent()
    }

    private fun initHandler() {
        handler = Handler(Looper.getMainLooper())
        binding.progressBar.visibility = View.VISIBLE
        binding.btnExample.visibility = View.GONE

        Thread {
            Thread.sleep(3000)
            handler.post {
                binding.progressBar.visibility = View.GONE
                val description = getString(R.string.describe).toString()
                speakMeDescription(description)
                Log.i(MYTAG, "Se ejecuta correctamente el hilo")
                binding.btnExample.visibility = View.VISIBLE
            }
        }.start()
    }

    private fun configureTextToSpeech() {
        textToSpeech = TextToSpeech(applicationContext, TextToSpeech.OnInitListener {
            if (it != TextToSpeech.ERROR) {
                textToSpeech.language = Locale.getDefault()
                Log.i(MYTAG, "Sin problemas en la configuración TextToSpeech")
            } else {
                Log.i(MYTAG, "Error en la configuración TextToSpeech")
            }
        })
    }

    private fun initEvent() {
        binding.btnExample.setOnClickListener {
            val currentTime = System.currentTimeMillis()
            if (currentTime - touchLastTime < TOUCH_MAX_TIME) {
                executorDoubleTouch()
                Log.i(MYTAG, "Escuchamos un chiste aleatorio")
            } else {
                speakMeDescription("Botón para escuchar un chiste")
                Log.i(MYTAG, "Hemos pulsado 1 vez.")
            }
            touchLastTime = currentTime
        }
    }

    private fun speakMeDescription(s: String) {
        Log.i(MYTAG, "Intenta hablar")
        textToSpeech.speak(s, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    private fun executorDoubleTouch() {
        val chisteAleatorio = chistes[Random.nextInt(chistes.size)]
        speakMeDescription(chisteAleatorio)
    }

    override fun onDestroy() {
        if (::textToSpeech.isInitialized) {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
        super.onDestroy()
    }
}
