package com.example.gestaopetcontrol

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gestaopetcontrol.databinding.ActivityClientesBinding
import com.example.gestaopetcontrol.databinding.ActivityMainBinding

class Clientes : AppCompatActivity() {

    private lateinit var binding: ActivityClientesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityClientesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        binding.btnRegistro.setOnClickListener {
            intent = Intent(this, ClientesRegistroInfo::class.java)
            startActivity(intent)
        }

        // Navegação

        binding.btnAgendamentos.setOnClickListener {
            intent = Intent(this, Agendamentos::class.java)
            startActivity(intent)
        }

        binding.btnCasa.setOnClickListener {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        binding.btnPets.setOnClickListener {
            intent = Intent(this, Pets::class.java)
            startActivity(intent)
        }

    }
}