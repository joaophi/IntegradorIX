package com.github.joaophi.integrador_ix.inicio

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import at.favre.lib.crypto.bcrypt.BCrypt
import com.github.joaophi.integrador_ix.Action
import com.github.joaophi.integrador_ix.database.IntegradorIXDatabase
import com.github.joaophi.integrador_ix.getStateFlow
import com.github.joaophi.integrador_ix.launchAction
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class LoginViewModel(
    application: Application,
    handle: SavedStateHandle
) : AndroidViewModel(application) {
    val dao = IntegradorIXDatabase.getDatabase(application).usuarioDao()

    val usuario by handle.getStateFlow(initialValue = "")
    val usuarioError = usuario.map {
        when {
            it.isBlank() -> "Campo deve ser preenchido"
            else -> null
        }
    }

    val senha by handle.getStateFlow(initialValue = "")
    val senhaError = senha.map {
        when {
            it.isBlank() -> "Campo deve ser preenchido"
            else -> null
        }
    }

    private val _login = MutableSharedFlow<Action>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val login = _login.asSharedFlow()

    object UsuarioOuSenhaInvalidos : Exception("Usuário ou senha inválidos")

    fun login() {
        viewModelScope.launchAction(_login) {
            listOf(usuarioError, senhaError)
                .onEach { flow -> flow.first()?.let { throw Exception(it) } }

            val usuario = dao.getUsuario(usuario.value) ?: throw UsuarioOuSenhaInvalidos
            if (!BCrypt.verifyer().verify(senha.value.toCharArray(), usuario.senha).verified)
                throw UsuarioOuSenhaInvalidos
        }
    }
}