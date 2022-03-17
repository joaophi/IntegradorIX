package com.github.joaophi.integrador_ix.projetos.projeto.requisito

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.github.joaophi.integrador_ix.*
import com.github.joaophi.integrador_ix.databinding.FragmentRequisitoBinding
import com.github.joaophi.integrador_ix.projetos.projeto.ProjetoViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach

class RequisitoFragment : Fragment(R.layout.fragment_requisito) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentRequisitoBinding.bind(view)
        val viewModel: ProjetoViewModel by navGraphViewModels(R.id.projetoFragment)
        val args: RequisitoFragmentArgs by navArgs()

        viewModel.carregarRequisito(args.id)

        requireActivity().addMenuProvider(viewLifecycleOwner, R.menu.save_delete) { item ->
            when (item.itemId) {
                R.id.btnSave -> viewModel.saveRequsito()
                R.id.btnDelete -> viewModel.deleteRequisito()
                else -> return@addMenuProvider false
            }
            true
        }

        binding.edtDescricao.bind(
            viewLifecycleOwner,
            viewModel.descricaoRequisito,
            viewModel.descricaoRequisitoError,
        )
        binding.edtImportancia.bind(
            viewLifecycleOwner,
            viewModel.importanciaRequisitoOpcoes,
            viewModel.importanciaRequisito,
            viewModel.importanciaRequisitoError,
        )
        binding.edtDificuldade.bind(
            viewLifecycleOwner,
            viewModel.dificuldadeRequisitoOpcoes,
            viewModel.dificuldadeRequisito,
            viewModel.dificuldadeRequisitoError,
        )
        binding.edtHoras.bindNumber(
            viewLifecycleOwner,
            viewModel.tempoRequisito,
            viewModel.tempoRequisitoError,
        )

        merge(viewModel.saveRequisito, viewModel.deleteRequisito)
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
    }
}