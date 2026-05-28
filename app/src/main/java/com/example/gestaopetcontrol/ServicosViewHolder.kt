package com.example.gestaopetcontrol

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ServicosViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val itemNome = itemView.findViewById<TextView>(R.id.linhaServico_txtNome)
    private val itemPreco = itemView.findViewById<TextView>(R.id.linhaServico_txtPreco)
    private val itemDescricao = itemView.findViewById<TextView>(R.id.linhaServico_txtDescricao)

    fun bind(item: ServicoData) {
        itemNome.text = item.nome
        itemPreco.text = "R$ %.2f".format(item.preco)
        itemDescricao.text = item.descricao

        itemView.setOnClickListener {
            item.onClick?.invoke(item.id)
        }

        itemView.setOnLongClickListener {
            item.onLongClick?.invoke(item.id)
            true
        }
    }
}
