package com.example.gestaopetcontrol

data class ClientesData (
    val nome: String,
    val cpf: String,
    val telefone: String,
    val endereço: String,
    val numero_residencia: Int,
    val complemento: String,
    val referencia: String,
    val cidade: String,
    val bairro: String,
    val estado: String,
    val apagado: Int = 0,
    val id: Int? = null,

    val onClick: ((Int?) -> Unit)? = null,
    val onLongClick: ((Int?) -> Unit)? = null

)