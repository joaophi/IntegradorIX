package com.github.joaophi.integrador_ix.projetos

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.github.joaophi.integrador_ix.R
import com.github.joaophi.integrador_ix.databinding.FragmentProjetosBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ProjetosFragment : Fragment(R.layout.fragment_projetos) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentProjetosBinding.bind(view)
        val navController = findNavController()
        val viewModel: ProjetosViewModel by viewModels()

        val adapter = ProjetoAdapter { projeto ->
            val action = ProjetosFragmentDirections
                .actionProjetosFragmentToProjetoFragment()
                .setId(projeto.id)
            navController.navigate(action)
        }
        binding.rvProjetos.adapter = adapter

        viewModel.projetos
            .onEach(adapter::submitList)
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .launchIn(viewLifecycleOwner.lifecycleScope)

        binding.btnNovo.setOnClickListener {
            navController.navigate(ProjetosFragmentDirections.actionProjetosFragmentToProjetoFragment())
        }
    }
}