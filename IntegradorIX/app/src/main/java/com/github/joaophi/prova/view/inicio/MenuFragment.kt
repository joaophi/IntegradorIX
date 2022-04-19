package com.github.joaophi.prova.view.inicio

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.joaophi.prova.R
import com.github.joaophi.prova.databinding.FragmentMenuBinding

class MenuFragment : Fragment(R.layout.fragment_menu) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentMenuBinding.bind(view)
        val navController = findNavController()

        binding.btnIncluir.setOnClickListener {
            navController.navigate(
                MenuFragmentDirections.actionMenuFragmentToIncluirFragment()
            )
        }
        binding.btnListar.setOnClickListener {
            navController.navigate(
                MenuFragmentDirections.actionMenuFragmentToListarFragment()
            )
        }
    }
}