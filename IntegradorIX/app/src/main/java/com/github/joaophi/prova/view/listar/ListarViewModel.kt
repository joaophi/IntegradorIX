package com.github.joaophi.prova.view.listar

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.github.joaophi.prova.database.ProvaDatabase
import com.github.joaophi.prova.database.model.Musica
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ListarViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = ProvaDatabase.getDatabase(application).musicaDao()

    val musicas = dao.musicas
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), initialValue = emptyList())

    fun excluir(musica: Musica) {
        viewModelScope.launch {
            dao.delete(musica)
        }
    }
}