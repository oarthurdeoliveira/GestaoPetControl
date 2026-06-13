package com.example.gestaopetcontrol

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gestaopetcontrol.banco.Database
import com.example.gestaopetcontrol.banco.apagarAgendamento
import com.example.gestaopetcontrol.banco.selecionarAgendamentos
import com.example.gestaopetcontrol.banco.selecionarClientes
import com.example.gestaopetcontrol.databinding.ActivityAgendamentosBinding

class Agendamentos : AppCompatActivity() {

    private lateinit var binding: ActivityAgendamentosBinding

    private val agendamentosadapter = AgendamentoAdapter()

    private lateinit var database: Database


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAgendamentosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        database = Database(this)


        binding.btnRegistroAgendamento.setOnClickListener {
            intent = Intent(this, AgendamentoRegistro::class.java)
            startActivity(intent)
        }

        binding.rvListaAgendamentos.layoutManager = LinearLayoutManager(this)
        binding.rvListaAgendamentos.adapter = agendamentosadapter


        binding.agendamentoIcon.imageTintList = ColorStateList.valueOf(
            "#909090".toColorInt()
        )
        binding.agendamentoText.setTextColor("#909090".toColorInt())

        //Navegação

        binding.clienteArea.setOnClickListener {
            intent = Intent(this, Clientes::class.java)
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


        binding.btnRegistroAgendamento.setOnClickListener {
            intent = Intent(this, AgendamentoRegistro::class.java)
            startActivity(intent)
        }

        binding.servicosArea.setOnClickListener {
            intent = Intent(this, Servicos::class.java)
            startActivity(intent)
        }

        binding.btnPesquisarCliente.setOnClickListener {
            pesquisaragendamento()
        }



    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()

        val sql = """
            SELECT AGENDAMENTOS.*, 
                   CLIENTES.NOME as NOME_DONO, 
                   CLIENTES.CPF as CPF_DONO, 
                   PETS.NOME as NOME_PET, 
                   PETS.RACA as RACA_PET, 
                   PETS.ESPECIE as ESPECIE_PET,
                   SERVICOS.NOME as NOME_SERVICO, 
                   SERVICOS.PRECO as PRECO_SERVICO
            FROM AGENDAMENTOS
            INNER JOIN PETS ON AGENDAMENTOS.ID_PET = PETS.ID
            INNER JOIN CLIENTES ON PETS.ID_CLIENTE = CLIENTES.ID
            LEFT JOIN SERVICOS ON AGENDAMENTOS.ID_SERVICO = SERVICOS.ID
            WHERE AGENDAMENTOS.APAGADO = 0
              AND (AGENDAMENTOS.DATA || ' ' || AGENDAMENTOS.HORA) >= datetime('now', 'localtime')
            ORDER BY (AGENDAMENTOS.DATA || ' ' || AGENDAMENTOS.HORA) ASC
        """.trimIndent()

        val agendamentodata = database.selecionarAgendamentos(sql).map {
            it.copy (onClick = ::editarAgendamentos, onLongClick = ::apagarAgendamentos)
        }
        agendamentosadapter.updateItens(agendamentodata)

    }

    private fun editarAgendamentos(idAgendamento: Int?) {
        val intent = Intent(this, AgendamentoRegistro::class.java)
        intent.putExtra("ID_AGENDAMENTO", idAgendamento)
        startActivity(intent)
    }

    private fun apagarAgendamentos(idAgendamento: Int?) {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Apagar Agendamento")
            .setMessage("Deseja apagar este agendamento?")
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Apagar") { _, _ ->
                val sql = """
                    SELECT AGENDAMENTOS.*, 
                           CLIENTES.NOME as NOME_DONO, 
                           CLIENTES.CPF as CPF_DONO, 
                           PETS.NOME as NOME_PET, 
                           PETS.RACA as RACA_PET, 
                           PETS.ESPECIE as ESPECIE_PET,
                           SERVICOS.NOME as NOME_SERVICO, 
                           SERVICOS.PRECO as PRECO_SERVICO
                    FROM AGENDAMENTOS
                    INNER JOIN PETS ON AGENDAMENTOS.ID_PET = PETS.ID
                    INNER JOIN CLIENTES ON PETS.ID_CLIENTE = CLIENTES.ID
                    LEFT JOIN SERVICOS ON AGENDAMENTOS.ID_SERVICO = SERVICOS.ID
                    WHERE AGENDAMENTOS.APAGADO = 0
                      AND (AGENDAMENTOS.DATA || ' ' || AGENDAMENTOS.HORA) >= datetime('now', 'localtime')
                    ORDER BY (AGENDAMENTOS.DATA || ' ' || AGENDAMENTOS.HORA) ASC
                """.trimIndent()
                database.apagarAgendamento(idAgendamento)
                val agendamentodata = database.selecionarAgendamentos(sql).map {
                    it.copy(onClick = ::editarAgendamentos, onLongClick = ::apagarAgendamentos)
                }
                agendamentosadapter.updateItens(agendamentodata)
                Toast.makeText(this, "Agendamento apagado", Toast.LENGTH_SHORT).show()
            }
            .show()
    }

    private fun pesquisaragendamento() {
        if (binding.edtCpfPesquisa.length() <= 0) {
            Toast.makeText(this, "Escreva um cpf para poder pesquisar", Toast.LENGTH_SHORT).show()
        }else {
            val termo = binding.edtCpfPesquisa.text.toString()
            val sql = """
            SELECT AGENDAMENTOS.*, 
                   CLIENTES.NOME as NOME_DONO, 
                   CLIENTES.CPF as CPF_DONO, 
                   PETS.NOME as NOME_PET, 
                   PETS.RACA as RACA_PET, 
                   PETS.ESPECIE as ESPECIE_PET,
                   SERVICOS.NOME as NOME_SERVICO, 
                   SERVICOS.PRECO as PRECO_SERVICO
            FROM AGENDAMENTOS
            INNER JOIN PETS ON AGENDAMENTOS.ID_PET = PETS.ID
            INNER JOIN CLIENTES ON PETS.ID_CLIENTE = CLIENTES.ID
            LEFT JOIN SERVICOS ON AGENDAMENTOS.ID_SERVICO = SERVICOS.ID
            WHERE AGENDAMENTOS.APAGADO = 0
            AND (AGENDAMENTOS.DATA || ' ' || AGENDAMENTOS.HORA) >= datetime('now', 'localtime')
            AND CLIENTES.CPF = '${termo}'
            ORDER BY (AGENDAMENTOS.DATA || ' ' || AGENDAMENTOS.HORA) ASC
        """.trimIndent()

            val agendamentoData = database.selecionarAgendamentos(sql).map {
                it.copy(onClick = ::editarAgendamentos, onLongClick = ::apagarAgendamentos)
            }

            if (agendamentoData.isEmpty()) {
                Toast.makeText(this, "Cliente não encontrado", Toast.LENGTH_SHORT).show()
            }

            agendamentosadapter.updateItens(agendamentoData)
        }
    }

}
