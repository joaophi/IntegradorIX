package com.github.joaophi.integrador_ix.projetos.projeto

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.github.joaophi.integrador_ix.Action
import com.github.joaophi.integrador_ix.R
import com.github.joaophi.integrador_ix.addMenuProvider
import com.github.joaophi.integrador_ix.bind
import com.github.joaophi.integrador_ix.databinding.FragmentProjetoBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach

class ProjetoFragment : Fragment(R.layout.fragment_projeto) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentProjetoBinding.bind(view)
        val navController = findNavController()
        val viewModel: ProjetoViewModel by navGraphViewModels(R.id.projetoFragment)

        binding.edtNome.bind(viewLifecycleOwner, viewModel.nome, viewModel.nomeError)
        binding.edtInicio.bind(viewLifecycleOwner, viewModel.inicio, viewModel.inicioError)
        binding.edtEstimativa
            .bind(viewLifecycleOwner, viewModel.estimativa, viewModel.estimativaError)

        val adapter = RequistioAdapter {
            val action = ProjetoFragmentDirections
                .actionProjetoFragmentToRequisitoFragment(it.idProjeto)
                .setId(it.id)
            navController.navigate(action)
        }
        binding.rvRequisitos.adapter = adapter
        viewModel.requisitos
            .onEach(adapter::submitList)
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .launchIn(viewLifecycleOwner.lifecycleScope)

        binding.btnNovo.setOnClickListener {
            val action = ProjetoFragmentDirections
                .actionProjetoFragmentToRequisitoFragment(viewModel.args.id)
            navController.navigate(action)
        }

        requireActivity().addMenuProvider(viewLifecycleOwner, R.menu.save_delete) { item ->
            when (item.itemId) {
                R.id.btnSave -> viewModel.save()
                R.id.btnDelete -> viewModel.delete()
                else -> return@addMenuProvider false
            }
            true
        }

        merge(viewModel.save, viewModel.delete)
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