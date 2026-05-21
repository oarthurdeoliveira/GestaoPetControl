package com.example.gestaopetcontrol

data class PetData(
    val nome: String,
    val especie: String,
    val raca: String,
    val idade: Int,
    val peso: Int,
    val alergias: String,
    val observacoes: String,
    val apagado: Int = 0,


    val idCliente: Int? = null,
    val cpf_cliente: String? = null,
    val nome_cliente: String? = null,
    val id: Int? = null,


    val onClick: ((Int?) -> Unit)? = null,
    val onLongClick: ((Int?) -> Unit)? = null
)
