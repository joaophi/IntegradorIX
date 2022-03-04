package com.github.joaophi.integrador_ix

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.github.joaophi.integrador_ix.databinding.ActivityInicioBinding
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun TextInputEditText.bind(lifecycle: Lifecycle, stateFlow: MutableStateFlow<String>) {
    stateFlow
        .filter { it != text.toString() }
        .onEach(::setText)
        .flowWithLifecycle(lifecycle)
        .launchIn(lifecycle.coroutineScope)
    doAfterTextChanged { stateFlow.value = it.toString() }
}

class InicioActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment: NavHostFragment = binding.navHost.getFragment()
        navController = navHostFragment.navController
        NavigationUI.setupActionBarWithNavController(activity = this, navController)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> navController.navigateUp()
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }
}