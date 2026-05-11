package com.example.gestaopetcontrol

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gestaopetcontrol.databinding.ActivityClienteRegistroPetsBinding

class ClienteRegistroPets : AppCompatActivity() {

    private lateinit var binding: ActivityClienteRegistroPetsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityClienteRegistroPetsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.btnFinalizar.setOnClickListener {
            intent = Intent(this, Clientes::class.java)
            startActivity(intent)
        }

    }
}