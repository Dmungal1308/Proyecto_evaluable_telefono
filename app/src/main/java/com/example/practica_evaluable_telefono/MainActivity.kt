package com.ejemplo.practica_evaluable_telefono

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.hardware.camera2.CameraManager
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.practica_evaluable_telefono.AlarmReceiver
import com.example.practica_evaluable_telefono.ChistesActivity
import com.example.practica_evaluable_telefono.ConfigActivity
import com.example.practica_evaluable_telefono.DadosActivity
import com.example.practica_evaluable_telefono.R

class MainActivity : AppCompatActivity() {

    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent
    private var encendida = false

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.gris)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val sistemaBarras = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(sistemaBarras.left, sistemaBarras.top, sistemaBarras.right, sistemaBarras.bottom)
            insets
        }

        val buttonScaleAnimation = AnimationUtils.loadAnimation(this, R.anim.botones_animacion)

        findViewById<ImageButton>(R.id.ib_llamada).setOnClickListener {
            it.startAnimation(buttonScaleAnimation)
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }

        findViewById<ImageButton>(R.id.ib_linterna).setOnClickListener {
            it.startAnimation(buttonScaleAnimation)
            linterna()
        }

        findViewById<ImageButton>(R.id.ib_alarma).setOnClickListener {
            it.startAnimation(buttonScaleAnimation)
            ponerAlarma()
        }

        findViewById<ImageButton>(R.id.ib_internet).setOnClickListener {
            it.startAnimation(buttonScaleAnimation)
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.example.com"))
            startActivity(webIntent)
        }

        findViewById<ImageButton>(R.id.ib_dados).setOnClickListener {
            it.startAnimation(buttonScaleAnimation)
            val intent = Intent(this, ConfigActivity::class.java)
            startActivity(intent)
        }


        findViewById<ImageButton>(R.id.ib_altavoz).setOnClickListener {
            it.startAnimation(buttonScaleAnimation)
            val intent = Intent(this, ChistesActivity::class.java)
            startActivity(intent)
        }
    }

    @SuppressLint("NewApi")
    private fun linterna() {
        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraId = cameraManager.cameraIdList[0] // Usar la primera cámara disponible

        try {
            encendida = !encendida // Cambiar el estado de la linterna
            cameraManager.setTorchMode(cameraId, encendida)
            Toast.makeText(this, if (encendida) "Linterna encendida" else "Linterna apagada", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error al activar la linterna", Toast.LENGTH_SHORT).show()
        }
    }

    private fun ponerAlarma() {
        alarmMgr = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmIntent = Intent(this, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }

        alarmMgr?.set(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + 10 * 1000,
            alarmIntent
        )

        Toast.makeText(this, "Alarma configurada para dentro de 2 min", Toast.LENGTH_SHORT).show()
    }
}
