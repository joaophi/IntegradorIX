package com.github.joaophi.integrador_ix.requisito

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.joaophi.integrador_ix.R
import com.github.joaophi.integrador_ix.addMenuProvider
import com.github.joaophi.integrador_ix.databinding.FragmentRequisitoBinding
import com.google.android.material.R as RM

class RequisitoFragment : Fragment(R.layout.fragment_requisito) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentRequisitoBinding.bind(view)

        fun limparErros() {
            binding.tilDescricao.error = null
            binding.tilImportancia.error = null
            binding.tilDificuldade.error = null
            binding.tilHoras.error = null
        }

        requireActivity().addMenuProvider(viewLifecycleOwner, R.menu.save_delete) { item ->
            when (item.itemId) {
                R.id.btnSave -> {
                    limparErros()
                    var erro = false

                    val descricao = binding.edtDescricao.text
                    if (descricao.isNullOrBlank()) {
                        binding.tilDescricao.error = "Campo em branco"
                        erro = true
                    }

                    val importancia = binding.edtImportancia.text?.toString()?.toIntOrNull()
                    if (importancia == null) {
                        binding.tilImportancia.error = "Campo em branco"
                        erro = true
                    }

                    val dificuldade = binding.edtDificuldade.text?.toString()?.toIntOrNull()
                    if (dificuldade == null) {
                        binding.tilDificuldade.error = "Campo em branco"
                        erro = true
                    }

                    val tempo = binding.edtHoras.text?.toString()?.toIntOrNull()
                    if (tempo == null) {
                        binding.tilHoras.error = "Campo em branco"
                        erro = true
                    }

                    if (!erro) {
                        Toast.makeText(
                            requireContext(),
                            """
                            Salvo:
                                Descrição: $descricao
                                Importância: $importancia
                                Dificultade: $dificuldade
                                Tempo(Horas): $tempo
                            """.trimIndent(),
                            Toast.LENGTH_LONG,
                        ).show()
                        findNavController().navigateUp()
                    }
                }
                R.id.btnDelete -> {
                    Toast.makeText(requireContext(), "Excluido", Toast.LENGTH_LONG).show()
                    findNavController().navigateUp()
                }
                else -> return@addMenuProvider false
            }
            true
        }

        val adapter = ArrayAdapter(
            requireContext(),
            RM.layout.support_simple_spinner_dropdown_item,
            (1..10).toList(),
        )
        binding.edtImportancia.setAdapter(adapter)
        binding.edtDificuldade.setAdapter(adapter)
    }
}