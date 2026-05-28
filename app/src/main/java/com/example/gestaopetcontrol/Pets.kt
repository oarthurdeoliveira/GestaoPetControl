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
import com.example.gestaopetcontrol.banco.apagarPet
import com.example.gestaopetcontrol.banco.selecionarClientes
import com.example.gestaopetcontrol.banco.selecionarPets
import com.example.gestaopetcontrol.databinding.ActivityPetsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class Pets : AppCompatActivity() {

    private lateinit var binding: ActivityPetsBinding

    private val petsadapter = PetsAdapter()
    private lateinit var database: Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPetsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnRegistrarPets.setOnClickListener {
            intent = Intent(this, PetsRegistro::class.java)
            startActivity(intent)
        }

        binding.rvListaPets.layoutManager = LinearLayoutManager(this)
        binding.rvListaPets.adapter = petsadapter
        database = Database(this)


        binding.petIcon.imageTintList = ColorStateList.valueOf(
            "#909090".toColorInt()
        )
        binding.petsText.setTextColor("#909090".toColorInt())

        //Navegação

        binding.clienteArea.setOnClickListener {
            intent = Intent(this, Clientes::class.java)
            startActivity(intent)
        }

        binding.homeArea.setOnClickListener {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.agendamentoArea.setOnClickListener {
            intent = Intent(this, Agendamentos::class.java)
            startActivity(intent)
        }

        binding.btnPesquisarClientePets.setOnClickListener {
            pesquisarpet()
        }

    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        val sql = """
        SELECT PETS.*, CLIENTES.NOME as CLIENTE_NOME, CLIENTES.CPF as CLIENTE_CPF 
        FROM PETS 
        INNER JOIN CLIENTES ON PETS.ID_CLIENTE = CLIENTES.ID
        WHERE PETS.APAGADO = 0
    """.trimIndent()
        val petsdata = database.selecionarPets(sql).map {
            it.copy (onClick = ::editarPet, onLongClick = ::apagarPet)
        }
        petsadapter.updateItens(petsdata)
    }

    private fun pesquisarpet() {

        if (binding.edtCpfClientePets.length() <= 0) {
            Toast.makeText(this, "Escreve o CPF do cliente para procurar os pets", Toast.LENGTH_SHORT).show()
        }else {
            val termo = binding.edtCpfClientePets.text.toString()
            val sql = """
                SELECT PETS.*, CLIENTES.NOME as CLIENTE_NOME, CLIENTES.CPF as CLIENTE_CPF 
                FROM PETS 
                INNER JOIN CLIENTES ON PETS.ID_CLIENTE = CLIENTES.ID
                WHERE PETS.APAGADO = 0
                AND (CLIENTES.CPF LIKE '%${termo}%' OR CLIENTES.NOME LIKE '%${termo}%' OR PETS.NOME LIKE '%${termo}%')
            """.trimIndent()

            val petsdata = database.selecionarPets(sql).map {
                it.copy (onClick = ::editarPet, onLongClick = ::apagarPet)
            }

            if (petsdata.isEmpty()) {
                Toast.makeText(this, "Cliente não encontrado", Toast.LENGTH_SHORT).show()
            }else {
                petsadapter.updateItens(petsdata)
            }

        }

    }

    private fun editarPet(idPet: Int?) {
        Toast.makeText(this, "${idPet}", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, PetsRegistro::class.java)
        intent.putExtra("ID_PET", idPet)
        startActivity(intent)
    }

    private fun apagarPet(idPet: Int?) {
        val sql = """
        SELECT PETS.*, CLIENTES.NOME as CLIENTE_NOME, CLIENTES.CPF as CLIENTE_CPF 
        FROM PETS 
        INNER JOIN CLIENTES ON PETS.ID_CLIENTE = CLIENTES.ID
        WHERE PETS.APAGADO = 0
    """.trimIndent()
        val alerta = AlertDialog.Builder(this)
        alerta.setTitle("Apagar Pet")
        alerta.setMessage("Deseja apagar Pet?")
        alerta.setCancelable(false)
        alerta.setPositiveButton("Apagar") {
                dialog, which ->
            database.apagarPet(idPet)
            val clientesData = database.selecionarPets(sql).map {
                it.copy(onClick = ::editarPet, onLongClick = ::apagarPet)
            }
            petsadapter.updateItens(clientesData)
        }
        alerta.setNegativeButton("Cancelar") {
                dialog, which ->
        }

        val alertaDialogo = alerta.create()
        alerta.show()
    }
}
