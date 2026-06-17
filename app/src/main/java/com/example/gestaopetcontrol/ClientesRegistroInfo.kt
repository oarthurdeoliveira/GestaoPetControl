package com.example.gestaopetcontrol

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gestaopetcontrol.banco.Database
import com.example.gestaopetcontrol.banco.atualizarCliente
import com.example.gestaopetcontrol.databinding.ActivityClientesRegistroInfoBinding
import com.example.gestaopetcontrol.banco.inserirCliente
import com.example.gestaopetcontrol.banco.pegaCliente

class ClientesRegistroInfo : AppCompatActivity() {

    private lateinit var binding: ActivityClientesRegistroInfoBinding
    private var idCliente: Int? = null
    private lateinit var database: Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityClientesRegistroInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.btnFinalizar.setOnClickListener {
            cadastrarCliente()
        }

        database = Database(this)
        idCliente = intent.getIntExtra("ID_CLIENTES", -1)
        if (idCliente == -1) {
            Toast.makeText(this, "Novo cliente", Toast.LENGTH_SHORT).show()
        }else {
            val cliente = database.pegaCliente(idCliente)
            //Toast.makeText(this, "{$cliente}", Toast.LENGTH_SHORT).show()
            if (cliente != null) {
                binding.edtNome.setText(cliente.nome)
                binding.edtCpf.setText(cliente.cpf)
                binding.edtTelefone.setText(cliente.telefone)
                binding.edtEndereco.setText(cliente.endereço)
                binding.ednNumero.setText(cliente.numero_residencia.toString())
                binding.edtComplemento.setText(cliente.complemento)
                binding.edtReferencia.setText(cliente.referencia)
                binding.edtBairro.setText(cliente.bairro)
                binding.edtCidade.setText(cliente.cidade)
                binding.edtEstado.setText(cliente.estado)

                binding.btnFinalizar.text = "Alterar"
                binding.btnFinalizar.isEnabled = true
            }
        }

    }

    fun cadastrarCliente(){
        val nome = binding.edtNome.text.toString().trim()
        val cpf = binding.edtCpf.text.toString().trim()
        val telefone = binding.edtTelefone.text.toString()
        val endereco = binding.edtEndereco.text.toString()
        val numero_residencia = binding.ednNumero.text.toString().toIntOrNull()
        val complemento = binding.edtComplemento.text.toString()
        val referencia = binding.edtReferencia.text.toString()
        val cidade = binding.edtCidade.text.toString()
        val bairro = binding.edtBairro.text.toString()
        val estado = binding.edtEstado.text.toString()

        var cliente: ClientesData? = null

        if (nome.isBlank() || cpf.isBlank()) {
            Toast.makeText(this, "Informe nome e CPF", Toast.LENGTH_SHORT).show()
            return
        }

        if (numero_residencia == null) {
            Toast.makeText(this, "Informe um numero de residencia valido", Toast.LENGTH_SHORT).show()
            return
        }

        // !! garante pro compilador que a variavel não é nula
        if(idCliente!! >= 0) {
            // Quando estou alterando um existente
            Toast.makeText(this, "Alterando cliente: $nome", Toast.LENGTH_SHORT).show()
            cliente = ClientesData(nome, cpf, telefone, endereco, numero_residencia, complemento, referencia, cidade, bairro, estado, 0, idCliente)
            val atualizado = database.atualizarCliente(cliente)
            finish()
        }else {
            // Quando estou inserindo um novo
            cliente = ClientesData(nome, cpf, telefone, endereco, numero_residencia, complemento, referencia, cidade, bairro, estado, 0)
            val idclienteRetornardo = database.inserirCliente(cliente)
            if(idclienteRetornardo == -1L) {
                Toast.makeText(this, "Error ao inserir aluno", Toast.LENGTH_SHORT).show()
            }else {
                Toast.makeText(this, "$nome cadastrado com sucesso", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }
}
