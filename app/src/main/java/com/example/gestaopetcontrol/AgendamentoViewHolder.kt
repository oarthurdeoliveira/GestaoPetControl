package com.example.gestaopetcontrol

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AgendamentoViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private var itemHora = itemView.findViewById<TextView>(R.id.linhaAgen_txtHora)
    private var itemNomePet = itemView.findViewById<TextView>(R.id.linhaAgen_PetNome)
    private var itemExtraPet = itemView.findViewById<TextView>(R.id.linhaAgen_PetExtra)
    private var itemNomeDono = itemView.findViewById<TextView>(R.id.linhaAgen_DonoNome)


    fun bind(item: AgendamentoData) {
        itemHora.text = item.hora
        itemNomePet.text = item.nome_pet
        itemNomeDono.text = item.nome_dono
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