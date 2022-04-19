package com.github.joaophi.prova.view.listar

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.github.joaophi.prova.R
import com.github.joaophi.prova.databinding.FragmentListarBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ListarFragment : Fragment(R.layout.fragment_listar) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentListarBinding.bind(view)
        val viewModel: ListarViewModel by viewModels()

        val adapter = MusicaAdapter {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Deseja realmente excluir?")
                .setNegativeButton("Não", null)
                .setPositiveButton("Sim") { _, _ -> viewModel.excluir(it) }
                .show()
        }
        binding.rvProjetos.adapter = adapter

        viewModel.musicas
            .onEach(adapter::submitList)
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }
}