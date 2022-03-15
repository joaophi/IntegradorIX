package com.github.joaophi.integrador_ix

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.github.joaophi.integrador_ix.databinding.ActivityInicioBinding

class InicioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment: NavHostFragment = binding.navHost.getFragment()
        val navController = navHostFragment.navController

        NavigationUI.setupActionBarWithNavController(activity = this, navController)
        addMenuProvider(owner = this) { menuItem ->
            when (menuItem.itemId) {
                android.R.id.home -> navController.navigateUp()
                else -> return@addMenuProvider false
            }
            true
        }
    }
}