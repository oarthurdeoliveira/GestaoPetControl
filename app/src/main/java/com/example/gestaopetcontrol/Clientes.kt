package com.example.gestaopetcontrol

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gestaopetcontrol.banco.Database
import com.example.gestaopetcontrol.banco.selecionarClientes
import com.example.gestaopetcontrol.databinding.ActivityClientesBinding
import com.example.gestaopetcontrol.databinding.ActivityMainBinding

class Clientes : AppCompatActivity() {

    private lateinit var binding: ActivityClientesBinding
    private val clientesAdapter = ClientesAdapter()
    private lateinit var database: Database

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


        binding.rvLista.layoutManager = LinearLayoutManager(this)
        binding.rvLista.adapter = clientesAdapter
        database = Database(this)




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

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()

        val clientesData = database.selecionarClientes().map {
            it.copy(onClick = ::editarCliente, onLongClick = ::apagarCliente)
        }
        clientesAdapter.updateItens(clientesData)
    }

    private fun editarCliente(idCliente: Int?) {
        Toast.makeText(this, "Clicou", Toast.LENGTH_SHORT).show()
    }

    private fun apagarCliente(idCliente: Int?) {
        Toast.makeText(this, "Click looongo", Toast.LENGTH_SHORT).show()
    }


}