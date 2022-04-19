package com.github.joaophi.prova.view.listar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.joaophi.prova.database.model.Musica
import com.github.joaophi.prova.databinding.ItemMusicaBinding

class MusicaAdapter(
    private val onExcluir: (Musica) -> Unit
) : ListAdapter<Musica, MusicaAdapter.MusicaViewHolder>(ItemCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MusicaViewHolder(
        ItemMusicaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: MusicaViewHolder, position: Int): Unit =
        holder.bind(getItem(position))

    inner class MusicaViewHolder(
        private val binding: ItemMusicaBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var musica: Musica

        init {
            binding.btnExcluir.setOnClickListener { onExcluir(musica) }
        }

        fun bind(item: Musica) {
            musica = item
            binding.tvNome.text = item.nome
            binding.tvAno.text = "${item.ano}"
            binding.tvArtistaAlbum.text = "${item.artista} - ${item.album}"
        }
    }
}

object ItemCallback : DiffUtil.ItemCallback<Musica>() {
    override fun areItemsTheSame(oldItem: Musica, newItem: Musica): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Musica, newItem: Musica): Boolean =
        oldItem == newItem
}