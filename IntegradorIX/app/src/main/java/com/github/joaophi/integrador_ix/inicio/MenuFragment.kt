package com.github.joaophi.integrador_ix.inicio

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.joaophi.integrador_ix.R
import com.github.joaophi.integrador_ix.databinding.FragmentMenuBinding

class MenuFragment : Fragment(R.layout.fragment_menu) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentMenuBinding.bind(view)
        binding.btnProjetos.setOnClickListener {
            findNavController().navigate(
                MenuFragmentDirections.actionMenuFragmentToProjetosFragment()
            )
        }
        binding.btnSair.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}