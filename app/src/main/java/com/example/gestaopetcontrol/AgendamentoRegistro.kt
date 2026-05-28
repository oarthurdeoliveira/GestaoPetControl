package com.example.gestaopetcontrol

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.example.gestaopetcontrol.banco.Database
import com.example.gestaopetcontrol.banco.atualizarAgendamento
import com.example.gestaopetcontrol.banco.inserirAgendamento
import com.example.gestaopetcontrol.banco.pegaClienteCPF
import com.example.gestaopetcontrol.banco.pegarAgendamento
import com.example.gestaopetcontrol.banco.selecionarServicos
import com.example.gestaopetcontrol.banco.selecionarPets
import com.example.gestaopetcontrol.databinding.ActivityAgendamentoRegistroBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AgendamentoRegistro : AppCompatActivity() {

    private lateinit var binding: ActivityAgendamentoRegistroBinding
    private val calendar = Calendar.getInstance()

    private var idAgendamento: Int? = null
    private var idPet: Int? = null
    private var idCliente: Int? = null
    private var idServico: Int? = null
    private var petsEncontrados = listOf<PetData>()
    private var servicosEncontrados = listOf<ServicoData>()
    private lateinit var database: Database


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
        idAgendamento = intent.getIntExtra("ID_AGENDAMENTO", -1)

        database = Database(this)

        binding.eddataDataAgendamento.setOnClickListener {
            datepicker()
        }

        binding.edtimeHora.setOnClickListener {
            hourpicker()
        }

        binding.btnAgendar.isEnabled = false
        binding.btnAgendar.isActivated = false
        binding.spinnerPetAgendamento.visibility = View.GONE
        binding.edtPetNomeSozinho.visibility = View.GONE
        carregarServicos()

        binding.spinnerPetAgendamento.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (petsEncontrados.isNotEmpty()) {
                    idPet = petsEncontrados[position].id
                }
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) = Unit
        }

        binding.spinnerServicoAgendamento.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (servicosEncontrados.isNotEmpty()) {
                    idServico = servicosEncontrados[position].id
                }
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) = Unit
        }

        binding.btnAgendar.setOnClickListener {
            cadastrarAgendamento()
        }


        binding.btnPesquisarClienteAgen.setOnClickListener {
            val cpf_cliente = binding.edtCpfAgendamento.text.toString()
            val data_cliente = database.pegaClienteCPF(cpf_cliente)

            if (data_cliente != null) {
                binding.edtNomeAgendamento.setText(data_cliente.nome)
                //id_cliente = data_cliente.id
                //Toast.makeText(this, "{$id_cliente}", Toast.LENGTH_SHORT).show()

                var sql = """
                    SELECT PETS.*, CLIENTES.NOME as CLIENTE_NOME, CLIENTES.CPF as CLIENTE_CPF 
                    FROM PETS 
                    INNER JOIN CLIENTES ON PETS.ID_CLIENTE = CLIENTES.ID
                    WHERE PETS.APAGADO = 0 AND CLIENTES.CPF = '${cpf_cliente}'
                
                """.trimIndent()

                petsEncontrados = database.selecionarPets(sql)

                if (petsEncontrados.isEmpty()) {
                    binding.btnAgendar.isEnabled = false
                    binding.btnAgendar.isActivated = false
                    binding.spinnerPetAgendamento.visibility = View.GONE
                    binding.edtPetNomeSozinho.visibility = View.GONE
                    Toast.makeText(this, "Cliente encontrado, mas sem pets cadastrados", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }

                if (petsEncontrados.size == 1) {
                    binding.spinnerPetAgendamento.visibility = View.GONE
                    binding.edtPetNomeSozinho.visibility = View.VISIBLE
                    binding.edtPetNomeSozinho.setText(petsEncontrados[0].nome)
                    idPet = petsEncontrados[0].id
                }else{
                    binding.edtPetNomeSozinho.visibility = View.GONE
                    binding.spinnerPetAgendamento.visibility = View.VISIBLE
                    val adapter = ArrayAdapter(
                        this,
                        android.R.layout.simple_spinner_item,
                        petsEncontrados.map { "${it.nome} - ${it.raca}" }
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerPetAgendamento.adapter = adapter
                    idPet = petsEncontrados[0].id
                }


                binding.btnAgendar.isEnabled = true
                binding.btnAgendar.isActivated = true
            } else {
                Toast.makeText(this, "Cliente com CPF inexistente", Toast.LENGTH_SHORT).show()
            }

        }

        if (idAgendamento == -1) {
            Toast.makeText(this,"Novo agendamento", Toast.LENGTH_SHORT).show()
        }else {
            val agendamento = database.pegarAgendamento(idAgendamento)
            if (agendamento != null) {
                binding.edtCpfAgendamento.setText(agendamento.cpf_dono)
                binding.edtNomeAgendamento.setText(agendamento.nome_dono)
                binding.edtPetNomeSozinho.visibility = View.VISIBLE
                binding.edtPetNomeSozinho.setText(agendamento.nome_pet)
                binding.spinnerPetAgendamento.visibility = View.GONE
                binding.eddataDataAgendamento.setText(agendamento.data)
                binding.edtimeHora.setText(agendamento.hora)
                binding.edtObsAgen.setText(agendamento.observacao)
                idPet = agendamento.idPet
                idServico = agendamento.idServico
                val posicaoServico = servicosEncontrados.indexOfFirst { it.id == agendamento.idServico }
                if (posicaoServico >= 0) {
                    binding.spinnerServicoAgendamento.setSelection(posicaoServico)
                }
                binding.btnAgendar.text = "Alterar Agendamento"
                binding.btnAgendar.isEnabled = true
                binding.btnAgendar.isActivated = true
            }
        }

    }


    fun cadastrarAgendamento() {
        val data = binding.eddataDataAgendamento.text.toString()
        val hora = binding.edtimeHora.text.toString()
        val observacao = binding.edtObsAgen.text.toString()

        if (idPet == null) {
            Toast.makeText(this, "Selecione ou pesquise um pet antes de agendar", Toast.LENGTH_SHORT).show()
            return
        }

        if (idServico == null) {
            Toast.makeText(this, "Selecione um servico antes de agendar", Toast.LENGTH_SHORT).show()
            return
        }

        if (data.isBlank() || hora.isBlank()) {
            Toast.makeText(this, "Informe data e hora", Toast.LENGTH_SHORT).show()
            return
        }

        val nome_pet = petsEncontrados.firstOrNull { it.id == idPet }?.nome
            ?: binding.edtPetNomeSozinho.text.toString()
        val nome_dono = binding.edtNomeAgendamento.text.toString()
        val cpf_dono = binding.edtCpfAgendamento.text.toString()
        val raca_pet = "todo"
        val especie_pet = "todo"

        var agendameto: AgendamentoData? = null

        if (idAgendamento!! >= 1){
            agendameto = AgendamentoData(data, hora, observacao, 0, nome_pet, raca_pet, especie_pet, nome_dono, cpf_dono, idPet, idAgendamento, idServico)
            database.atualizarAgendamento(agendameto)
            Toast.makeText(this, "Agendamento alterado com sucesso", Toast.LENGTH_SHORT).show()
            finish()
        }else {
            agendameto = AgendamentoData(data, hora, observacao, 0, nome_pet, raca_pet, especie_pet, nome_dono, cpf_dono, idPet, null, idServico)
            val agendamento_criar = database.inserirAgendamento(agendameto)
            if (agendamento_criar == -1L) {
                Toast.makeText(this, "Error inserir agendamento", Toast.LENGTH_SHORT).show()
            }else {
                Toast.makeText(this, "Agendamento cadastrado com sucesso", Toast.LENGTH_SHORT).show()
            }
            finish()
        }

    }

    private fun carregarServicos() {
        servicosEncontrados = database.selecionarServicos()
        if (servicosEncontrados.isEmpty()) {
            binding.btnAgendar.isEnabled = false
            binding.btnAgendar.isActivated = false
            Toast.makeText(this, "Cadastre um servico antes de agendar", Toast.LENGTH_LONG).show()
            return
        }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            servicosEncontrados.map { "${it.nome} - R$ %.2f".format(it.preco) }
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerServicoAgendamento.adapter = adapter
        idServico = servicosEncontrados[0].id
    }


    fun datepicker(){
        val dataPickerDialog = DatePickerDialog(this, {DataPicker, year: Int, month: Int, day: Int ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, day)
            val dateformat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
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
            val formattedTime = String.format("%02d:%02d:00", hora, minuto)
            binding.edtimeHora.setText(formattedTime)
        },
            calendar.get(Calendar.HOUR),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }


}
