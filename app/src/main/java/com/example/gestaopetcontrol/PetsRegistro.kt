package com.example.gestaopetcontrol

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gestaopetcontrol.banco.Database
import com.example.gestaopetcontrol.banco.atualizarPet
import com.example.gestaopetcontrol.banco.inserirPet
import com.example.gestaopetcontrol.banco.pegaCliente
import com.example.gestaopetcontrol.banco.pegaClienteCPF
import com.example.gestaopetcontrol.banco.pegarPet
import com.example.gestaopetcontrol.databinding.ActivityPetsRegistroBinding

class PetsRegistro : AppCompatActivity() {

    private lateinit var binding: ActivityPetsRegistroBinding
    private lateinit var database: Database
    private var idPet: Int? = null
    private var id_cliente: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPetsRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        idPet = intent.getIntExtra("ID_PET", -1)

        database = Database(this)
        binding.btnSalvarPet.isEnabled = false
        binding.btnSalvarPet.isActivated = false

        if (idPet == -1) {
            Toast.makeText(this, "Novo Pet", Toast.LENGTH_SHORT).show()
        }else{
            // editar pet existente
            Toast.makeText(this, "Pet existente", Toast.LENGTH_SHORT).show()
            val pet = database.pegarPet(idPet)

            if (pet != null) {
                id_cliente = pet.idCliente
                binding.edtCpfTutor.setText(pet.cpf_cliente)
                binding.edtNomeClientePets.setText(pet.nome_cliente)

                binding.edtNomePet.setText(pet.nome)
                binding.edtEspeciePet.setText(pet.especie)
                binding.edtRacaPet.setText(pet.raca)
                binding.edtIdadePet.setText(pet.idade.toString())
                binding.edtPesoPet.setText(pet.peso.toString())
                binding.edtAlergiasPet.setText(pet.alergias)
                binding.edtObservacoesPet.setText(pet.observacoes)
                binding.btnSalvarPet.text = "Alterar Pet"
                binding.btnSalvarPet.isEnabled = true
                binding.btnSalvarPet.isActivated = true
            }
        }


        binding.btnPesquisarClientePet.setOnClickListener {
            val cpf_cliente = binding.edtCpfTutor.text.toString()
            val data_cliente = database.pegaClienteCPF(cpf_cliente)

            if (data_cliente != null) {
                binding.edtNomeClientePets.setText(data_cliente.nome)
                id_cliente = data_cliente.id
                Toast.makeText(this, "{$id_cliente}", Toast.LENGTH_SHORT).show()
                binding.btnSalvarPet.isEnabled = true
                binding.btnSalvarPet.isActivated = true
            } else {
                Toast.makeText(this, "Cliente com CPF inexistente", Toast.LENGTH_SHORT).show()
            }

        }

        binding.btnSalvarPet.setOnClickListener {
            cadastrarPet()
        }
    }

    private fun cadastrarPet() {
        //Toast.makeText(this, "teste", Toast.LENGTH_SHORT).show()

        val nome_pet = binding.edtNomePet.text.toString()
        val especie = binding.edtEspeciePet.text.toString()
        val raca = binding.edtRacaPet.text.toString()
        val idade = binding.edtIdadePet.text.toString().toIntOrNull()
        val peso = binding.edtPesoPet.text.toString().toIntOrNull()
        val alergias = binding.edtAlergiasPet.text.toString()
        val observacoes = binding.edtObservacoesPet.text.toString()

        var pet: PetData? = null

        if (nome_pet.isBlank()) {
            Toast.makeText(this, "Informe o nome do pet", Toast.LENGTH_SHORT).show()
            return
        }

        if (idade == null || peso == null) {
            Toast.makeText(this, "Informe idade e peso validos", Toast.LENGTH_SHORT).show()
            return
        }

        if (idPet!! >= 1) {
            // Editando um existente
            pet = PetData(nome_pet, especie, raca, idade, peso, alergias, observacoes, 0, id_cliente, null, null, idPet)
            val atualizar = database.atualizarPet(pet)
            Toast.makeText(this, "${atualizar}", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            // Criando um novo
            pet = PetData(nome_pet, especie, raca, idade, peso, alergias, observacoes, 0, id_cliente)
            val pet_criar = database.inserirPet(pet)
            if (pet_criar == -1L) {
                Toast.makeText(this, "Error ao inserir pet", Toast.LENGTH_SHORT).show()
            }else {
                Toast.makeText(this, "Pet $nome_pet inserido com sucesso!", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }

}
