package com.github.joaophi.integrador_ix.inicio

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.github.joaophi.integrador_ix.Action
import com.github.joaophi.integrador_ix.R
import com.github.joaophi.integrador_ix.bind
import com.github.joaophi.integrador_ix.databinding.FragmentRegistrarUsuarioBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class RegistrarUsuarioFragment : Fragment(R.layout.fragment_registrar_usuario) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentRegistrarUsuarioBinding.bind(view)
        val viewModel: RegistrarUsuarioViewModel by viewModels()

        binding.edtUsuario.bind(viewLifecycleOwner, viewModel.usuario, viewModel.usuarioError)
        binding.edtSenha.bind(viewLifecycleOwner, viewModel.senha, viewModel.senhaError)
        binding.edtSenhaRepetida.bind(viewLifecycleOwner, viewModel.senhaRepetida, viewModel.senhaRepetidaError)

        binding.btnRegistrar.setOnClickListener { viewModel.registrar() }

        viewModel.registrar
            .onEach {
                when (it) {
                    Action.Running -> Unit
                    is Action.Error -> {
                        val text = "Erro: ${it.throwable.message}"
                        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
                    }
                    Action.Done -> {
                        findNavController().navigate(
                            RegistrarUsuarioFragmentDirections.actionRegistrarUsuarioFragmentToMenuFragment()
                        )
                    }
                }
            }
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }
}