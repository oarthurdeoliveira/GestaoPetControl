package com.example.gestaopetcontrol

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class PetsAdapter: RecyclerView.Adapter<PetsViewHolder>() {


    private var itens = listOf<PetData>()

    fun updateItens(newItens: List<PetData>){
        itens = newItens
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        p0: ViewGroup,
        p1: Int
    ): PetsViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.linha_pets, p0, false)
        return PetsViewHolder(view)
    }

    override fun onBindViewHolder(p0: PetsViewHolder, p1: Int) {
        p0.bind(itens[p1])
    }

    override fun getItemCount(): Int = itens.size
}