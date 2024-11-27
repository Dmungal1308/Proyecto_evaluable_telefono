package com.example.practica_evaluable_telefono

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class ConfigActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.gris)
        // Configurar Spinner
        val spinner = findViewById<Spinner>(R.id.spinner_tiempo)
        val options = arrayOf("1 seg", "2 seg", "3 seg")
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, options)

        // Configurar SeekBar
        val seekBar = findViewById<SeekBar>(R.id.seekBar_dados)
        val textNumDados = findViewById<TextView>(R.id.text_num_dados)
        seekBar.max = 2 // Valores entre 0 y 2 (1, 2, 3 dados)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                textNumDados.text = "Número de Dados: ${progress + 1}"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        // Configurar RadioGroup
        val radioGroup = findViewById<RadioGroup>(R.id.radio_group)

        // Configurar Botón para iniciar el juego
        val startGameButton = findViewById<Button>(R.id.btn_start_game)
        startGameButton.setOnClickListener {
            val selectedTime = spinner.selectedItem.toString()
            val numDados = seekBar.progress + 1
            val apuesta = findViewById<EditText>(R.id.edit_apuesta).text.toString().toIntOrNull()
            val modoJuego = when (radioGroup.checkedRadioButtonId) {
                R.id.radio_suma -> "SUMA"
                R.id.radio_multiplicador -> "MULTIPLICADOR"
                else -> "SUMA"
            }

            if (apuesta == null) {
                Toast.makeText(this, "Introduce un número válido en tu apuesta", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, DadosActivity::class.java)
            intent.putExtra("TIME_BETWEEN_ROLLS", selectedTime)
            intent.putExtra("NUM_DADOS", numDados)
            intent.putExtra("APUESTA", apuesta)
            intent.putExtra("MODO_JUEGO", modoJuego)
            startActivity(intent)
        }
    }
}
