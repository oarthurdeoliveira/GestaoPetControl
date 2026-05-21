package com.example.gestaopetcontrol

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.Spinner
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.example.gestaopetcontrol.banco.Database
import com.example.gestaopetcontrol.banco.inserirAgendamento
import com.example.gestaopetcontrol.banco.pegaClienteCPF
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

        binding.btnAgendar.setOnClickListener {
            cadastrarAgendamento()
        }


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

                val pets_data = database.selecionarPets(sql)
                //val itens = listOf<String>()


                Toast.makeText(this, "${pets_data[0].nome}", Toast.LENGTH_SHORT).show()

                if (pets_data.size == 1) {
                    binding.edtPetNomeSozinho.visibility = View.VISIBLE
                    binding.edtPetNomeSozinho.setText(pets_data[0].nome)
                    idPet = pets_data[0].id
                }else{
                    //TODO: FAZER MAIS DE UM PET SPINNER
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
            // Existente
        }

    }


    fun cadastrarAgendamento() {
        val data = binding.eddataDataAgendamento.text.toString()
        val hora = binding.edtimeHora.text.toString()
        val observacao = binding.edtObsAgen.text.toString()
        val nome_pet = binding.edtPetNomeSozinho.text.toString()
        val nome_dono = binding.edtNomeAgendamento.text.toString()
        val cpf_dono = binding.edtNomeAgendamento.text.toString()
        val raca_pet = "todo"
        val especie_pet = "todo"

        var agendameto: AgendamentoData? = null

        if (idAgendamento!! >= 1){
            //editando existente
        }else {
            agendameto = AgendamentoData(data, hora, observacao, 0, nome_pet, raca_pet, especie_pet, nome_dono, cpf_dono, idPet)
            val agendamento_criar = database.inserirAgendamento(agendameto)
            if (agendamento_criar == 1L) {
                Toast.makeText(this, "Error inserir agendamento", Toast.LENGTH_SHORT).show()
            }else {
                Toast.makeText(this, "OK agendamento", Toast.LENGTH_SHORT).show()
            }
            finish()
        }

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
