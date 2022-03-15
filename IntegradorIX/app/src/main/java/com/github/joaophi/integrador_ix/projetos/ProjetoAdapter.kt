package com.github.joaophi.integrador_ix.projetos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.joaophi.integrador_ix.database.model.Projeto
import com.github.joaophi.integrador_ix.databinding.ItemProjetoBinding
import java.time.format.DateTimeFormatter

class ProjetoAdapter(
    val onClick: (Projeto) -> Unit,
) : ListAdapter<Projeto, ProjetoAdapter.ProjetoViewHolder>(ItemCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProjetoViewHolder(
        ItemProjetoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ProjetoViewHolder, position: Int): Unit =
        holder.bind(getItem(position))

    private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    inner class ProjetoViewHolder(
        private val binding: ItemProjetoBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var projeto: Projeto

        init {
            binding.root.setOnClickListener { onClick(projeto) }
        }

        fun bind(item: Projeto) {
            projeto = item
            binding.tvNome.text = item.nome
            binding.tvDescricao.text =
                "${formatter.format(item.inicio)} até ${formatter.format(item.estimativaConclusao)}"
        }
    }
}

object ItemCallback : DiffUtil.ItemCallback<Projeto>() {
    override fun areItemsTheSame(oldItem: Projeto, newItem: Projeto): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Projeto, newItem: Projeto): Boolean =
        oldItem == newItem
}