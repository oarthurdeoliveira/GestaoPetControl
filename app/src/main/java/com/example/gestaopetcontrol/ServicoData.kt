package com.example.gestaopetcontrol

data class ServicoData(
    val nome: String,
    val preco: Double,
    val descricao: String,
    val apagado: Int = 0,
    val id: Int? = null,
    val onClick: ((Int?) -> Unit)? = null,
    val onLongClick: ((Int?) -> Unit)? = null
)
