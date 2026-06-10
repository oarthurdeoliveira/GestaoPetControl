package com.example.gestaopetcontrol

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class AgendamentoViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private var itemHora = itemView.findViewById<TextView>(R.id.linhaAgen_txtHora)
    private var itemNomePet = itemView.findViewById<TextView>(R.id.linhaAgen_PetNome)
    private var itemExtraPet = itemView.findViewById<TextView>(R.id.linhaAgen_PetExtra)
    private var itemServicoNome = itemView.findViewById<TextView>(R.id.linhaAgen_ServicoNome)


    fun bind(item: AgendamentoData) {
        val hora_formatada = LocalTime.parse(item.hora)
        itemHora.text = hora_formatada.format(DateTimeFormatter.ofPattern("HH:mm"))
        itemNomePet.text = item.nome_pet
        itemServicoNome.text = item.nome_servico
        val servico = item.nome_servico ?: "Servico nao informado"
        itemExtraPet.text = "${item.especie_pet} - ${item.raca_pet}"

        itemView.setOnClickListener {
            item.onClick?.invoke(item.id)
        }
        itemView.setOnLongClickListener {
            item.onLongClick?.invoke(item.id)
            true
        }
    }

}
