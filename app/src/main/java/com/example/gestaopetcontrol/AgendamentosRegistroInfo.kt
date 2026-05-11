package com.example.gestaopetcontrol

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gestaopetcontrol.databinding.ActivityAgendamentosRegistroInfoBinding
import com.example.gestaopetcontrol.databinding.ActivityClientesRegistroInfoBinding

class AgendamentosRegistroInfo : AppCompatActivity() {

    private lateinit var binding: ActivityAgendamentosRegistroInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAgendamentosRegistroInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Precisa do Cliente colocar o nome do cliente primeiro
        binding.edtPetCliente.isEnabled = false

        binding.btnContinuarAgendamento.setOnClickListener {
            intent = Intent(this, AgendamentoRegistroServicos::class.java)
            startActivity(intent)
        }

    }
}