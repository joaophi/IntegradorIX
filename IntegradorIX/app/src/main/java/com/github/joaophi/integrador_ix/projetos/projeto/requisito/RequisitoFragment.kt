package com.github.joaophi.integrador_ix.projetos.projeto.requisito

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.bumptech.glide.Glide
import com.github.joaophi.integrador_ix.*
import com.github.joaophi.integrador_ix.databinding.FragmentRequisitoBinding
import com.github.joaophi.integrador_ix.projetos.projeto.ProjetoViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import java.io.File

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

        viewModel.latitudeRequisito
            .onEach { binding.tvLatitude.text = "Latitude: $it" }
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.longitudeRequisito
            .onEach { binding.tvLongitude.text = "Longitude: $it" }
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .launchIn(viewLifecycleOwner.lifecycleScope)

        val fotoDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        viewModel.foto1Requisito
            .onEach {
                Glide.with(requireContext())
                    .load(it?.let { File(fotoDir, it) })
                    .placeholder(R.drawable.ic_baseline_add_a_photo_24)
                    .error(R.drawable.ic_baseline_add_a_photo_24)
                    .into(binding.ivImage1)
            }
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.foto2Requisito
            .onEach {
                Glide.with(requireContext())
                    .load(it?.let { File(fotoDir, it) })
                    .placeholder(R.drawable.ic_baseline_add_a_photo_24)
                    .error(R.drawable.ic_baseline_add_a_photo_24)
                    .into(binding.ivImage2)
            }
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .launchIn(viewLifecycleOwner.lifecycleScope)


        val launchCamera = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it) viewModel.foto1Requisito.value = viewModel.tempFoto1Requisito.value
        }

        binding.ivImage1.setOnClickListener {
            val file = File.createTempFile("img_", ".jpg", fotoDir)
            viewModel.tempFoto1Requisito.value = file.name
            launchCamera.launch(Uri.fromFile(file))
        }

        binding.ivImage1.setOnLongClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setMessage("Remover imagem?")
                .setNegativeButton("Não", null)
                .setPositiveButton("Sim") { _, _ -> viewModel.foto1Requisito.value = null }
                .show()
            true
        }

        val launchCamera2 = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it) viewModel.foto2Requisito.value = viewModel.tempFoto2Requisito.value
        }

        binding.ivImage2.setOnClickListener {
            val file = File.createTempFile("img_", ".jpg", fotoDir)
            viewModel.tempFoto2Requisito.value = file.name
            launchCamera2.launch(Uri.fromFile(file))
        }

        binding.ivImage2.setOnLongClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setMessage("Remover imagem?")
                .setNegativeButton("Não", null)
                .setPositiveButton("Sim") { _, _ -> viewModel.foto2Requisito.value = null }
                .show()
            true
        }

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