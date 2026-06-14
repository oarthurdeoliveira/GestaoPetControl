package com.example.gestaopetcontrol

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gestaopetcontrol.banco.Database
import com.example.gestaopetcontrol.banco.apagarCliente
import com.example.gestaopetcontrol.banco.apagarClienteEmCascata
import com.example.gestaopetcontrol.banco.pegaCliente
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


        binding.clienteIcon.imageTintList = ColorStateList.valueOf(
            "#909090".toColorInt()
        )

        binding.clienteText.setTextColor("#909090".toColorInt())


        binding.btnRegistro.setOnClickListener {
            intent = Intent(this, ClientesRegistroInfo::class.java)
            startActivity(intent)
        }

        // Navegação

        binding.agendamentoArea.setOnClickListener {
            intent = Intent(this, Agendamentos::class.java)
            startActivity(intent)
        }

        binding.homeArea.setOnClickListener {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        binding.petArea.setOnClickListener {
            intent = Intent(this, Pets::class.java)
            startActivity(intent)
        }

        binding.servicosArea.setOnClickListener {
            intent = Intent(this, Servicos::class.java)
            startActivity(intent)
        }

        //outro

        binding.btnPesquisarCliente.setOnClickListener {
            pesquisarcliente()
        }

    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        val sql = "SELECT * FROM CLIENTES WHERE APAGADO = 0 ORDER BY NOME"
        val clientesData = database.selecionarClientes(sql).map {
            it.copy(onClick = ::editarCliente, onLongClick = ::apagarCliente)
        }
        clientesAdapter.updateItens(clientesData)
    }


    private fun pesquisarcliente() {
        if (binding.edtCpfPesquisa.length() <= 0) {
            Toast.makeText(this, "Escreva um cpf para poder pesquisar", Toast.LENGTH_SHORT).show()
        }else {
            val termo = binding.edtCpfPesquisa.text.toString()
            val sql = """
                SELECT * FROM CLIENTES
                WHERE APAGADO = 0
                AND (CPF LIKE '%${termo}%' OR NOME LIKE '%${termo}%' OR TELEFONE LIKE '%${termo}%')
                ORDER BY NOME
            """.trimIndent()
            val clientesData = database.selecionarClientes(sql).map {
                it.copy(onClick = ::editarCliente, onLongClick = ::apagarCliente)
            }

            if (clientesData.isEmpty()) {
                Toast.makeText(this, "Cliente não encontrado", Toast.LENGTH_SHORT).show()
            }

            clientesAdapter.updateItens(clientesData)
        }
    }

    private fun editarCliente(idCliente: Int?) {
        val intent = Intent(this, ClientesRegistroInfo::class.java)
        intent.putExtra("ID_CLIENTES", idCliente)
        startActivity(intent)
    }

    private fun apagarCliente(idCliente: Int?) {
        val alerta = AlertDialog.Builder(this)
        alerta.setTitle("Apagar Cliente")
        alerta.setMessage("Deseja apagar cliente?")
        alerta.setCancelable(false)
        alerta.setPositiveButton("Apagar") {
            dialog, which ->
            database.apagarClienteEmCascata(idCliente)
            val sql = "SELECT * FROM CLIENTES WHERE APAGADO = 0 ORDER BY NOME"
            val clientesData = database.selecionarClientes(sql).map {
                it.copy(onClick = ::editarCliente, onLongClick = ::apagarCliente)
            }
            clientesAdapter.updateItens(clientesData)
        }
        alerta.setNegativeButton("Cancelar") {
            dialog, which ->
        }

        val alertaDialogo = alerta.create()
        alerta.show()


    }


}
