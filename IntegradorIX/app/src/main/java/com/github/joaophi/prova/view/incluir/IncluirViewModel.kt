package com.github.joaophi.prova.view.incluir

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.github.joaophi.prova.Action
import com.github.joaophi.prova.database.ProvaDatabase
import com.github.joaophi.prova.database.model.Musica
import com.github.joaophi.prova.getStateFlow
import com.github.joaophi.prova.launchAction
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.Year

class IncluirViewModel(
    application: Application,
    handle: SavedStateHandle
) : AndroidViewModel(application) {
    private val dao = ProvaDatabase.getDatabase(application).musicaDao()

    val nome by handle.getStateFlow(initialValue = "")
    val nomeError = nome.map {
        when {
            it.isBlank() -> "Campo deve ser preenchido"
            else -> null
        }
    }

    val artista by handle.getStateFlow(initialValue = "")
    val artistaError = artista.map {
        when {
            it.isBlank() -> "Campo deve ser preenchido"
            else -> null
        }
    }

    val album by handle.getStateFlow(initialValue = "")
    val albumError = album.map {
        when {
            it.isBlank() -> "Campo deve ser preenchido"
            else -> null
        }
    }

    val ano by handle.getStateFlow<Int?>(initialValue = null)
    val anoError = ano.map {
        when (it) {
            !in -10_000..Year.now().value -> "Ano inválido"
            else -> null
        }
    }

    private val _save = MutableSharedFlow<Action>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val save = _save.asSharedFlow()

    fun save() {
        viewModelScope.launchAction(_save) {
            listOf(nomeError, artistaError, albumError, anoError)
                .onEach { flow -> flow.first()?.let { throw Exception(it) } }

            val nome = nome.value
            val artista = artista.value
            val album = album.value
            val ano = ano.value!!.toLong()

            dao.upsert(Musica(id = 0, nome, artista, album, ano))
        }
    }
}