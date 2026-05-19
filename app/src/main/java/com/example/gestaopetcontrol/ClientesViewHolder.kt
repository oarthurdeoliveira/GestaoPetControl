package com.example.gestaopetcontrol

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ClientesViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val itemNome = itemView.findViewById<TextView>(R.id.linha_txtNome)
    private val itemCpf = itemView.findViewById<TextView>(R.id.linha_txtCpf)


    fun bind(item: ClientesData){
        itemNome.text = item.nome
        itemCpf.text = item.cpf

        itemView.setOnClickListener {
            item.onClick?.invoke(item.id)
        }
        itemView.setOnLongClickListener {
            item.onLongClick?.invoke(item.id)
            true
        }

    }
}