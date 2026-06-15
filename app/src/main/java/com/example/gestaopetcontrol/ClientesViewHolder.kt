package com.example.gestaopetcontrol

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gestaopetcontrol.banco.Database
import com.example.gestaopetcontrol.banco.pegaQuantidadePetsCliente

class ClientesViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val itemNome = itemView.findViewById<TextView>(R.id.linha_txtNome)
    private val itemQuantidadePets = itemView.findViewById<TextView>(R.id.linha_txtQuantidadePets)

    private val itemExtra = itemView.findViewById<TextView>(R.id.linhaClienteExtra)

    private val database: Database = Database(view.context)

    fun bind(item: ClientesData){
        itemNome.text = item.nome
        val quantidade_pet = database.pegaQuantidadePetsCliente(item.cpf)
        if (quantidade_pet <= 1) {
            itemQuantidadePets.text = "Possui ${quantidade_pet.toString()} Pet"
        }else {
            itemQuantidadePets.text = "Possui ${quantidade_pet.toString()} Pets"
        }

        itemExtra.text = item.telefone

        itemView.setOnClickListener {
            item.onClick?.invoke(item.id)
        }
        itemView.setOnLongClickListener {
            item.onLongClick?.invoke(item.id)
            true
        }

    }
}