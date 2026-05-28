package com.example.gestaopetcontrol

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ServicosAdapter : RecyclerView.Adapter<ServicosViewHolder>() {

    private var itens = listOf<ServicoData>()

    fun updateItens(newItens: List<ServicoData>) {
        itens = newItens
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicosViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.linha_servicos, parent, false)
        return ServicosViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServicosViewHolder, position: Int) {
        holder.bind(itens[position])
    }

    override fun getItemCount(): Int = itens.size
}
