package com.github.joaophi.integrador_ix.projetos

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.github.joaophi.integrador_ix.database.model.Projeto

class ProjetoAdapter(
    val onClick: (Projeto) -> Unit,
) : ListAdapter<Projeto, ProjetoAdapter.ProjetoViewHolder>(ItemCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProjetoViewHolder(
        TODO()
    )

    override fun onBindViewHolder(holder: ProjetoViewHolder, position: Int): Unit =
        holder.bind(getItem(position))

    inner class ProjetoViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var projeto: Projeto

        init {
            binding.root.setOnClickListener { onClick(projeto) }
        }

        fun bind(item: Projeto) {
            projeto = item
        }
    }
}

object ItemCallback : DiffUtil.ItemCallback<Projeto>() {
    override fun areItemsTheSame(oldItem: Projeto, newItem: Projeto): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Projeto, newItem: Projeto): Boolean =
        oldItem == newItem
}