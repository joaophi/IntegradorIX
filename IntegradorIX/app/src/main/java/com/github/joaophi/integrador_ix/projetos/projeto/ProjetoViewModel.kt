package com.github.joaophi.integrador_ix.projetos.projeto

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.github.joaophi.integrador_ix.Action
import com.github.joaophi.integrador_ix.database.IntegradorIXDatabase
import com.github.joaophi.integrador_ix.database.model.Projeto
import com.github.joaophi.integrador_ix.database.model.Requisito
import com.github.joaophi.integrador_ix.getStateFlow
import com.github.joaophi.integrador_ix.launchAction
import com.github.joaophi.integrador_ix.locationFlow
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
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

    val link by handle.getStateFlow(initialValue = "")

    val requisitos by handle.getStateFlow(initialValue = emptyList<Requisito>())
    val requisitosError = requisitos.map {
        when {
            it.isEmpty() -> "Adicione ao menos um requisito"
            else -> null
        }
    }

    init {
        if ("init" !in handle) viewModelScope.launch {
            handle["init"] = true
            val projeto = dao.getProjeto(args.id) ?: return@launch
            nome.value = projeto.nome
            inicio.value = dateFormatter.format(projeto.inicio)
            estimativa.value = dateFormatter.format(projeto.estimativaConclusao)
            link.value = projeto.link.orEmpty()
            requisitos.value = dao.getRequisitos(args.id)
        }
    }

    private val _save = MutableSharedFlow<Action>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val save = _save.asSharedFlow()

    fun save() {
        viewModelScope.launchAction(_save) {
            listOf(nomeError, inicioError, estimativaError, requisitosError)
                .onEach { flow -> flow.first()?.let { throw Exception(it) } }

            val id = args.id
            val nome = nome.value
            val inicio = LocalDate.parse(inicio.value, dateFormatter)
            val estimativa = LocalDate.parse(estimativa.value, dateFormatter)
            val requisitos = requisitos.value
            val link = link.value.takeIf { it.isNotBlank() }

            dao.save(Projeto(id, nome, inicio, estimativa, link), requisitos)
        }
    }

    private val _delete = MutableSharedFlow<Action>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val delete = _delete.asSharedFlow()

    fun delete() {
        viewModelScope.launchAction(_delete) {
            dao.delete(args.id)
        }
    }

    // REQUISITO
    private var idRequisito = 0

    val descricaoRequisito by handle.getStateFlow(initialValue = "")
    val descricaoRequisitoError = descricaoRequisito.map {
        when {
            it.isBlank() -> "Campo deve ser preenchido"
            else -> null
        }
    }

    val importanciaRequisitoOpcoes = flowOf((1..10).toList())
    val importanciaRequisito by handle.getStateFlow<Int?>(initialValue = null)
    val importanciaRequisitoError = importanciaRequisito.map {
        when {
            it == null -> "Campo deve ser preenchido"
            else -> null
        }
    }

    val dificuldadeRequisitoOpcoes = flowOf((1..10).toList())
    val dificuldadeRequisito by handle.getStateFlow<Int?>(initialValue = null)
    val dificuldadeRequisitoError = dificuldadeRequisito.map {
        when {
            it == null -> "Campo deve ser preenchido"
            else -> null
        }
    }

    val tempoRequisito by handle.getStateFlow<Int?>(initialValue = null)
    val tempoRequisitoError = tempoRequisito.map {
        when {
            it == null -> "Campo deve ser preenchido"
            it <= 0 -> "Campo deve ser maior que zero"
            else -> null
        }
    }

    private val carregaLocalizacao = MutableSharedFlow<Boolean>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    init {
        val locationProvider = LocationServices.getFusedLocationProviderClient(application)
        carregaLocalizacao
            .flatMapLatest { carregaLocalizacao ->
                when {
                    carregaLocalizacao -> locationProvider
                        .locationFlow(LocationRequest.create().setPriority(PRIORITY_HIGH_ACCURACY))
                    else -> emptyFlow()
                }
            }
            .onEach {
                latitudeRequisito.value = it.latitude
                longitudeRequisito.value = it.longitude
            }
            .launchIn(viewModelScope)
    }

    val latitudeRequisito by handle.getStateFlow<Double?>(initialValue = null)
    val longitudeRequisito by handle.getStateFlow<Double?>(initialValue = null)

    val tempFoto1Requisito by handle.getStateFlow<String?>(initialValue = null)
    val tempFoto2Requisito by handle.getStateFlow<String?>(initialValue = null)
    val foto1Requisito by handle.getStateFlow<String?>(initialValue = null)
    val foto2Requisito by handle.getStateFlow<String?>(initialValue = null)

    fun carregarRequisito(id: Int) {
        idRequisito = id
        val requisito = requisitos.value.find { it.id == idRequisito }
        if (requisito == null) {
            descricaoRequisito.value = ""
            importanciaRequisito.value = null
            dificuldadeRequisito.value = null
            tempoRequisito.value = null
            latitudeRequisito.value = null
            longitudeRequisito.value = null
            carregaLocalizacao.tryEmit(true)
            foto1Requisito.value = null
            foto2Requisito.value = null
        } else {
            descricaoRequisito.value = requisito.descricao
            importanciaRequisito.value = requisito.importancia
            dificuldadeRequisito.value = requisito.dificuldade
            tempoRequisito.value = requisito.horasParaConclucao
            carregaLocalizacao.tryEmit(false)
            latitudeRequisito.value = requisito.latitude
            longitudeRequisito.value = requisito.longitude
            foto1Requisito.value = requisito.foto1
            foto2Requisito.value = requisito.foto2
        }
    }

    private val _saveRequisito = MutableSharedFlow<Action>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val saveRequisito = _saveRequisito.asSharedFlow()

    fun saveRequsito() {
        viewModelScope.launchAction(_saveRequisito) {
            listOf(
                descricaoRequisitoError,
                importanciaRequisitoError,
                dificuldadeRequisitoError,
                tempoRequisitoError,
            ).onEach { flow -> flow.first()?.let { throw Exception(it) } }

            requisitos.value = requisitos.value.filter { it.id != idRequisito } + Requisito(
                args.id,
                idRequisito.takeIf { it > 0 } ?: requisitos.value.count() + 1,
                descricaoRequisito.value,
                LocalDate.now(),
                importanciaRequisito.value!!,
                dificuldadeRequisito.value!!,
                tempoRequisito.value!!,
                latitudeRequisito.value,
                longitudeRequisito.value,
                foto1Requisito.value,
                foto2Requisito.value,
            )
            carregaLocalizacao.tryEmit(false)
        }
    }

    private val _deleteRequisito = MutableSharedFlow<Action>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val deleteRequisito = _deleteRequisito.asSharedFlow()

    fun deleteRequisito() {
        viewModelScope.launchAction(_deleteRequisito) {
            requisitos.value = requisitos.value.filter { it.id != idRequisito }
        }
    }
}