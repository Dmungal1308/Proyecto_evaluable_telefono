package com.ejemplo.practica_evaluable_telefono

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.practica_evaluable_telefono.R

class MainActivity2 : AppCompatActivity() {

    private val REQUEST_CALL_PERMISSION = 1
    private lateinit var editTextPhoneNumber: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = resources.getColor(R.color.gris)

        editTextPhoneNumber = findViewById(R.id.editTextText)
        val callButton = findViewById<ImageButton>(R.id.ib_coger_llamada)
        val buttonScaleAnimation = AnimationUtils.loadAnimation(this, R.anim.botones_animacion)
        cargarNumeroTelefono()

        callButton.setOnClickListener {
            val numeroTelefono = editTextPhoneNumber.text.toString()
            it.startAnimation(buttonScaleAnimation)
            hacerLlamada(numeroTelefono)
            guardarNumeroTelefono(numeroTelefono)
        }
    }

    private fun hacerLlamada(numeroTelefono: String) {
        if (numeroTelefono.isEmpty()) {
            Toast.makeText(this, "Pon un número de teléfono", Toast.LENGTH_SHORT).show()
            return
        }

        if (!numeroTelefono.matches(Regex("^[+]?[0-9]{9}$"))) {
            Toast.makeText(this, "Número de teléfono no válido", Toast.LENGTH_SHORT).show()
            return
        }


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CALL_PHONE), REQUEST_CALL_PERMISSION)
            } else {
                startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:$numeroTelefono")))
            }
        } else {
            startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:$numeroTelefono")))
        }
    }


    override fun onRequestPermissionsResult(codigo: Int, permisos: Array<out String>, resultados: IntArray) {
        super.onRequestPermissionsResult(codigo, permisos, resultados)
        if (codigo == REQUEST_CALL_PERMISSION) {
            if (resultados.isNotEmpty() && resultados[0] == PackageManager.PERMISSION_GRANTED) {
                val numeroTelefono = editTextPhoneNumber.text.toString()
                hacerLlamada(numeroTelefono)
            } else {
                Toast.makeText(this, "Permisos denegados.", Toast.LENGTH_SHORT).show()
                abrirPermisos()
            }
        }
    }

    private fun abrirPermisos() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:$packageName")
        startActivity(intent)
    }

    private fun guardarNumeroTelefono(numeroTelefono: String) {
        val sharedPreferences = getSharedPreferences("mi_app_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("numero_telefono", numeroTelefono)
        editor.apply()
    }

    private fun cargarNumeroTelefono() {
        val sharedPreferences = getSharedPreferences("mi_app_prefs", MODE_PRIVATE)
        val numeroTelefono = sharedPreferences.getString("numero_telefono", "")
        editTextPhoneNumber.setText(numeroTelefono)
    }
}
