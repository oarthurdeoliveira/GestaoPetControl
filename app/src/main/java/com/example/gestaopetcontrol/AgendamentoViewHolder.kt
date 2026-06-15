package com.example.gestaopetcontrol

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date

class AgendamentoViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private var itemHora = itemView.findViewById<TextView>(R.id.linhaAgen_txtHora)
    private var itemNomePet = itemView.findViewById<TextView>(R.id.linhaAgen_PetNome)
    private var itemExtraPet = itemView.findViewById<TextView>(R.id.linhaAgen_PetExtra)
    private var itemServicoNome = itemView.findViewById<TextView>(R.id.linhaAgen_ServicoNome)

    private var itemBordaConteudo = itemView.findViewById<LinearLayout>(R.id.borda_conteudo)


    fun bind(item: AgendamentoData) {
        val hora_formatada = LocalTime.parse(item.hora)
        val formatterEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formatterSaida   = DateTimeFormatter.ofPattern("dd/MM/yy")
        val data = LocalDate.parse(item.data, formatterEntrada)

        val dataformatada = data.format(formatterSaida)
        itemHora.text = "${hora_formatada.format(DateTimeFormatter.ofPattern("HH:mm"))} - ${dataformatada} "
        itemNomePet.text = item.nome_pet
        itemServicoNome.text = item.nome_servico
        val servico = item.nome_servico ?: "Servico nao informado"
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
