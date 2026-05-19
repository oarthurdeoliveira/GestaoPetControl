package com.example.gestaopetcontrol

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ClientesAdapter: RecyclerView.Adapter<ClientesViewHolder>() {

    private var itens = listOf<ClientesData>()

    fun updateItens(newItens: List<ClientesData>){
        itens = newItens
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        p0: ViewGroup,
        p1: Int
    ): ClientesViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.linha_clientes, p0, false)
        return ClientesViewHolder(view)
    }

    override fun onBindViewHolder(
        p0: ClientesViewHolder,
        p1: Int
    ) {
        p0.bind(itens[p1])
    }

    override fun getItemCount(): Int = itens.size
}