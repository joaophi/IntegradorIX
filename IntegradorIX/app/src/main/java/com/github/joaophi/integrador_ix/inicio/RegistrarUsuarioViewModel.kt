package com.github.joaophi.integrador_ix.inicio

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import at.favre.lib.crypto.bcrypt.BCrypt
import com.github.joaophi.integrador_ix.Action
import com.github.joaophi.integrador_ix.database.IntegradorIXDatabase
import com.github.joaophi.integrador_ix.database.model.Usuario
import com.github.joaophi.integrador_ix.getStateFlow
import com.github.joaophi.integrador_ix.launchAction
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

class RegistrarUsuarioViewModel(
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

    val senhaRepetida by handle.getStateFlow(initialValue = "")
    val senhaRepetidaError = combine(senha, senhaRepetida) { senha, senhaRepetida ->
        when {
            senhaRepetida.isBlank() -> "Campo deve ser preenchido"
            senhaRepetida != senha -> "Senha diferente"
            else -> null
        }
    }

    private val _registrar = MutableSharedFlow<Action>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val registrar = _registrar.asSharedFlow()

    fun registrar() {
        viewModelScope.launchAction(_registrar) {
            listOf(usuarioError, senhaError, senhaRepetidaError)
                .onEach { flow -> flow.first()?.let { throw Exception(it) } }

            val _usuario = dao.getUsuario(usuario.value)
            if (_usuario != null)
                throw Exception("Usuário já existe")

            dao.insertUsuario(
                Usuario(
                    usuario.value,
                    senha = BCrypt.withDefaults().hashToString(12, senha.value.toCharArray())
                )
            )
        }
    }
}