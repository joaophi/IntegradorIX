package com.github.joaophi.integrador_ix

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.annotation.MenuRes
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.LifecycleOwner

fun MenuHost.addMenuProvider(
    owner: LifecycleOwner,
    @MenuRes menuRes: Int,
    onMenuItemSelected: (MenuItem) -> Boolean,
) {
    addMenuProvider(object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater): Unit =
            menuInflater.inflate(menuRes, menu)

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean = onMenuItemSelected(menuItem)
    }, owner)
}