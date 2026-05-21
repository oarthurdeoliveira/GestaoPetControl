package com.example.gestaopetcontrol

data class AgendamentoData(
    val data: String,
    val hora: String,
    val observacao: String,
    val apagado: Int = 0,

    val nome_pet: String,
    val raca_pet: String,
    val especie_pet: String,

    val nome_dono: String,
    val cpf_dono: String,

    val idPet: Int? = null,
    val id: Int? = null,

    val onClick: ((Int?) -> Unit)? = null,
    val onLongClick: ((Int?) -> Unit)? = null
)
