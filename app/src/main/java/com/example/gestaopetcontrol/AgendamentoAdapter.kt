package com.example.gestaopetcontrol

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class AgendamentoAdapter: RecyclerView.Adapter<AgendamentoViewHolder>() {

    private var itens = listOf<AgendamentoData>()


    fun updateItens(newItens: List<AgendamentoData>) {
        itens = newItens
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        p0: ViewGroup,
        p1: Int
    ): AgendamentoViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.linha_agendamentos, p0, false)
        return AgendamentoViewHolder(view)
    }

    override fun onBindViewHolder(
        p0: AgendamentoViewHolder,
        p1: Int
    ) {
        p0.bind(itens[p1])
    }

    override fun getItemCount(): Int = itens.size

}