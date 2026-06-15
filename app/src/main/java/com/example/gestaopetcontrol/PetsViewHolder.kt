package com.example.gestaopetcontrol

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PetsViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val itemNomePet = itemView.findViewById<TextView>(R.id.linhaPet_txtNomepet)
    private val itemNomeCliente = itemView.findViewById<TextView>(R.id.linhaPet_txtNomeCliente)

    private val itemPetExtra = itemView.findViewById<TextView>(R.id.linhaPet_Extra)


    fun bind(item: PetData) {
        itemNomePet.text = item.nome
        itemNomeCliente.text = "${item.nome_cliente}"
        itemPetExtra.text = "${item.especie} - ${item.raca}"

        itemView.setOnClickListener {
            item.onClick?.invoke(item.id)
        }
        itemView.setOnLongClickListener {
            item.onLongClick?.invoke(item.id)
            true
        }
    }


}