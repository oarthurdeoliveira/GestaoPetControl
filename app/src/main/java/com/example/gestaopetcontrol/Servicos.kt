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
import com.example.gestaopetcontrol.banco.apagarServico
import com.example.gestaopetcontrol.banco.atualizarServico
import com.example.gestaopetcontrol.banco.inserirServico
import com.example.gestaopetcontrol.banco.pegarServico
import com.example.gestaopetcontrol.banco.selecionarServicos
import com.example.gestaopetcontrol.databinding.ActivityServicosBinding

class Servicos : AppCompatActivity() {

    private lateinit var binding: ActivityServicosBinding
    private lateinit var database: Database
    private val servicosAdapter = ServicosAdapter()
    private var idServicoEditando: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityServicosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        database = Database(this)

        binding.rvListaServicos.layoutManager = LinearLayoutManager(this)
        binding.rvListaServicos.adapter = servicosAdapter

        binding.btnSalvarServico.setOnClickListener { salvarServico() }
        binding.btnLimparServico.setOnClickListener { limparCampos() }

        binding.homeArea.setOnClickListener {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.clienteArea.setOnClickListener {
            intent = Intent(this, Clientes::class.java)
            startActivity(intent)

        }

        binding.petArea.setOnClickListener {
            intent = Intent(this, Pets::class.java)
            startActivity(intent)
        }

        binding.agendamentoArea.setOnClickListener {
            intent = Intent(this, Agendamentos::class.java)
            startActivity(intent)
        }

        binding.servicoIcon.imageTintList = ColorStateList.valueOf(
            "#909090".toColorInt()
        )

        binding.servicoText.setTextColor("#909090".toColorInt())

        carregarServicos()
    }

    private fun carregarServicos() {
        val lista = database.selecionarServicos().map {
            it.copy(onClick = ::editarServico, onLongClick = ::confirmarExclusao)
        }
        servicosAdapter.updateItens(lista)
    }

    private fun salvarServico() {
        val nome = binding.edtNomeServico.text.toString().trim()
        val preco = binding.edtPrecoServico.text.toString().replace(",", ".").toDoubleOrNull()
        val descricao = binding.edtDescricaoServico.text.toString().trim()

        if (nome.isBlank() || preco == null || preco <= 0.0) {
            Toast.makeText(this, "Informe nome e preco valido", Toast.LENGTH_SHORT).show()
            return
        }

        val servico = ServicoData(nome, preco, descricao, 0, idServicoEditando)
        if (idServicoEditando == null) {
            database.inserirServico(servico)
        } else {
            database.atualizarServico(servico)
        }

        limparCampos()
        carregarServicos()
        Toast.makeText(this, "Servico salvo com sucesso", Toast.LENGTH_SHORT).show()
    }

    private fun editarServico(idServico: Int?) {
        val servico = database.pegarServico(idServico)
        if (servico == null) {
            Toast.makeText(this, "Servico nao encontrado", Toast.LENGTH_SHORT).show()
            return
        }

        idServicoEditando = servico.id
        binding.edtNomeServico.setText(servico.nome)
        binding.edtPrecoServico.setText(servico.preco.toString())
        binding.edtDescricaoServico.setText(servico.descricao)
        binding.btnSalvarServico.text = "Alterar Servico"
    }

    private fun confirmarExclusao(idServico: Int?) {
        AlertDialog.Builder(this)
            .setTitle("Excluir servico")
            .setMessage("Deseja excluir este servico?")
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Excluir") { _, _ ->
                database.apagarServico(idServico)
                carregarServicos()
                limparCampos()
                Toast.makeText(this, "Servico excluido", Toast.LENGTH_SHORT).show()
            }
            .show()
    }

    private fun limparCampos() {
        idServicoEditando = null
        binding.edtNomeServico.text.clear()
        binding.edtPrecoServico.text.clear()
        binding.edtDescricaoServico.text.clear()
        binding.btnSalvarServico.text = "Salvar Servico"
    }
}
