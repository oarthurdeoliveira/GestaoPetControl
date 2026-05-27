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
import com.example.gestaopetcontrol.banco.selecionarAgendamentos
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



    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()

        val agendamentodata = database.selecionarAgendamentos().map {
            it.copy (onClick = ::editarAgendamentos, onLongClick = ::apagarAgendamentos)
        }
        agendamentosadapter.updateItens(agendamentodata)

    }

    private fun editarAgendamentos(idAgendamento: Int?) {
        Toast.makeText(this,"click curto ${idAgendamento}", Toast.LENGTH_SHORT).show()
    }

    private fun apagarAgendamentos(idAgendamento: Int?) {
        Toast.makeText(this,"click loongo ${idAgendamento}", Toast.LENGTH_SHORT).show()
    }

}