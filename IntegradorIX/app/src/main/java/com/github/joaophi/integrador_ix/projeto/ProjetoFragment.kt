package com.github.joaophi.integrador_ix.projeto

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.joaophi.integrador_ix.R
import com.github.joaophi.integrador_ix.addMenuProvider
import com.github.joaophi.integrador_ix.databinding.FragmentProjetoBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class ProjetoFragment : Fragment(R.layout.fragment_projeto) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentProjetoBinding.bind(view)

        fun limparErros() {
            binding.tilNome.error = null
            binding.tilInicio.error = null
            binding.tilEstimativa.error = null
        }

        fun limpar() {
            limparErros()
            binding.edtNome.text = null
            binding.edtInicio.text = null
            binding.edtEstimativa.text = null
        }

        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        requireActivity().addMenuProvider(viewLifecycleOwner, R.menu.save_delete) { item ->
            when (item.itemId) {
                R.id.btnSave -> {
                    limparErros()
                    var erro = false

                    val nome = binding.edtNome.text
                    if (nome.isNullOrBlank()) {
                        binding.tilNome.error = "Campo em branco"
                        erro = true
                    }

                    val inicio = try {
                        LocalDate.parse(binding.edtInicio.text, dateFormatter)
                    } catch (ex: DateTimeParseException) {
                        binding.tilInicio.error = "Data inválida"
                        erro = true
                        null
                    }

                    val estimativa = try {
                        LocalDate.parse(binding.edtEstimativa.text, dateFormatter)
                    } catch (ex: DateTimeParseException) {
                        binding.tilEstimativa.error = "Data inválida"
                        erro = true
                        null
                    }

                    if (inicio != null && estimativa != null && estimativa < inicio) {
                        binding.tilEstimativa.error = "Estimativa menor que inicio"
                        erro = true
                    }

                    if (!erro) {
                        Toast.makeText(
                            requireContext(),
                            """
                            Salvo:
                                Nome: $nome
                                Inicio: $inicio
                                Estimativa: $estimativa
                            """.trimIndent(),
                            Toast.LENGTH_LONG,
                        ).show()
                        limpar()
                    }
                }
                R.id.btnDelete -> {
                    Toast.makeText(requireContext(), "Excluido", Toast.LENGTH_LONG).show()
                    limpar()
                }
                else -> return@addMenuProvider false
            }
            true
        }

        binding.btnRequisito.setOnClickListener {
            findNavController().navigate(R.id.action_projetoFragment_to_requisitoFragment)
        }
    }
}