package com.example.gestaopetcontrol

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.example.gestaopetcontrol.databinding.ActivityAgendamentoRegistroBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AgendamentoRegistro : AppCompatActivity() {

    private lateinit var binding: ActivityAgendamentoRegistroBinding
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAgendamentoRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnAgendar.setOnClickListener {
            intent = Intent(this, Agendamentos::class.java)
            startActivity(intent)
        }


        binding.eddataDataAgendamento.setOnClickListener {
            datepicker()
        }

        binding.edtimeHora.setOnClickListener {
            hourpicker()
        }

        //TODO: CPF FORMATAÇÃO - 000.000.000-00

    }

    fun datepicker(){
        val dataPickerDialog = DatePickerDialog(this, {DataPicker, year: Int, month: Int, day: Int ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, day)
            val dateformat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formateddate = dateformat.format(selectedDate.time)
            binding.eddataDataAgendamento.setText(formateddate)
        },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)

        ).show()
    }

    fun hourpicker() {


        val picker = TimePickerDialog(this,{ _, hora, minuto ->
            val formattedTime = String.format("%02d:%02d", hora, minuto)
            binding.edtimeHora.setText(formattedTime)
        },
            calendar.get(Calendar.HOUR),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }


}
