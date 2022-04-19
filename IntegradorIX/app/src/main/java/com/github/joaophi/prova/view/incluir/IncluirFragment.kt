package com.github.joaophi.prova.view.incluir

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.github.joaophi.prova.*
import com.github.joaophi.prova.databinding.FragmentIncluirBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class IncluirFragment : Fragment(R.layout.fragment_incluir) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentIncluirBinding.bind(view)
        val viewModel: IncluirViewModel by viewModels()

        requireActivity().addMenuProvider(viewLifecycleOwner, R.menu.save_delete) { item ->
            when (item.itemId) {
                R.id.btnSave -> viewModel.save()
                R.id.btnDelete -> findNavController().navigateUp()
                else -> return@addMenuProvider false
            }
            true
        }

        binding.edtNome.bind(
            viewLifecycleOwner,
            viewModel.nome,
            viewModel.nomeError,
        )
        binding.edtArtista.bind(
            viewLifecycleOwner,
            viewModel.artista,
            viewModel.artistaError,
        )
        binding.edtAlbum.bind(
            viewLifecycleOwner,
            viewModel.album,
            viewModel.albumError,
        )
        binding.edtAno.bindNumber(
            viewLifecycleOwner,
            viewModel.ano,
            viewModel.anoError,
        )

        viewModel.save
            .onEach {
                when (it) {
                    Action.Running -> Unit
                    is Action.Error -> {
                        val text = "Erro: ${it.throwable.message}"
                        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
                    }
                    Action.Done -> {
                        findNavController().navigateUp()
                        Toast.makeText(requireContext(), "Ok", Toast.LENGTH_LONG).show()
                    }
                }
            }
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .launchIn(viewLifecycleOwner.lifecycleScope)

        binding.edtNome.requestFocus()
    }
}