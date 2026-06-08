package com.example.gestaopetcontrol

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PetsViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val itemNomePet = itemView.findViewById<TextView>(R.id.linhaPet_txtNomepet)
    private val itemNomeCliente = itemView.findViewById<TextView>(R.id.linhaPet_txtNomeCliente)


    fun bind(item: PetData) {
        itemNomePet.text = item.nome
        itemNomeCliente.text = item.nome_cliente

        itemView.setOnClickListener {
            item.onClick?.invoke(item.id)
        }
        itemView.setOnLongClickListener {
            item.onLongClick?.invoke(item.id)
            true
        }
    }


}