package com.github.joaophi.integrador_ix.projetos.projeto

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.joaophi.integrador_ix.database.model.Requisito
import com.github.joaophi.integrador_ix.databinding.ItemRequisitoBinding
import java.time.format.DateTimeFormatter

class RequistioAdapter(
    val onClick: (Requisito) -> Unit,
) : ListAdapter<Requisito, RequistioAdapter.RequisitoViewHolder>(ItemCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RequisitoViewHolder(
        ItemRequisitoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: RequisitoViewHolder, position: Int): Unit =
        holder.bind(getItem(position))

    private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    inner class RequisitoViewHolder(
        private val binding: ItemRequisitoBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var requisito: Requisito

        init {
            binding.root.setOnClickListener { onClick(requisito) }
        }

        fun bind(item: Requisito) {
            requisito = item
            binding.tvNome.text = item.descricao
            binding.tvDescricao.text =
                "Importância: ${item.importancia} Difiuldade: ${item.dificuldade}"
            binding.tvData.text = formatter.format(item.criacao)
            binding.tvConclusao.text = "${item.horasParaConclucao} horas para concluir"
        }
    }
}

object ItemCallback : DiffUtil.ItemCallback<Requisito>() {
    override fun areItemsTheSame(oldItem: Requisito, newItem: Requisito): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Requisito, newItem: Requisito): Boolean =
        oldItem == newItem
}