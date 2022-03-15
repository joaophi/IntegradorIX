package com.github.joaophi.integrador_ix.projetos.projeto

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.github.joaophi.integrador_ix.database.IntegradorIXDatabase
import com.github.joaophi.integrador_ix.database.model.Projeto
import com.github.joaophi.integrador_ix.database.model.Requisito
import com.github.joaophi.integrador_ix.getStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ProjetoViewModel(
    application: Application,
    handle: SavedStateHandle
) : AndroidViewModel(application) {
    private val dao = IntegradorIXDatabase.getDatabase(application).projetoDao()

    val args = ProjetoFragmentArgs.fromSavedStateHandle(handle)

    val nome by handle.getStateFlow(initialValue = "")
    val nomeError = nome.map {
        when {
            it.isBlank() -> "Campo deve ser preenchido"
            else -> null
        }
    }

    private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    val inicio by handle.getStateFlow(initialValue = "")
    val inicioError = inicio.map {
        when {
            it.isBlank() -> "Campo deve ser preenchido"
            runCatching { LocalDate.parse(it, dateFormatter) }.isFailure -> "Data inválida"
            else -> null
        }
    }

    val estimativa by handle.getStateFlow(initialValue = "")
    val estimativaError = combine(estimativa, inicio) { _estimativa, _inicio ->
        when {
            _estimativa.isBlank() -> "Campo deve ser preenchido"
            else -> {
                val inicio = runCatching { LocalDate.parse(_inicio, dateFormatter) }.getOrNull()
                val estimativa = runCatching { LocalDate.parse(_estimativa, dateFormatter) }
                    .getOrNull() ?: return@combine "Data inválida"

                when {
                    inicio != null && estimativa < inicio -> "Estimativa menor que inicio"
                    else -> null
                }
            }
        }
    }

    val requisitos by handle.getStateFlow(initialValue = emptyList<Requisito>())
    val requisitosError = requisitos.map {
        when {
            it.isEmpty() -> "Adicione ao menos um requisito"
            else -> null
        }
    }

    init {
        if ("requisitos" !in handle) viewModelScope.launch {
            requisitos.value = dao.getRequisitos(args.id)
        }
    }

    fun save() {
        viewModelScope.launch {
            nomeError.first()?.let { throw Exception(it) }
            inicioError.first()?.let { throw Exception(it) }
            estimativaError.first()?.let { throw Exception(it) }
            requisitosError.first()?.let { throw Exception(it) }

            val id = args.id
            val nome = nome.value
            val inicio = LocalDate.parse(inicio.value, dateFormatter)
            val estimativa = LocalDate.parse(estimativa.value, dateFormatter)
            val requisitos = requisitos.value

            dao.save(Projeto(id, nome, inicio, estimativa), requisitos)
        }
    }

    fun delete() {
        viewModelScope.launch {
            dao.delete(args.id)
        }
    }
}