package com.github.joaophi.integrador_ix.projetos

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.github.joaophi.integrador_ix.database.IntegradorIXDatabase
import com.github.joaophi.integrador_ix.database.model.Projeto
import kotlinx.coroutines.flow.Flow

class ProjetosViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = IntegradorIXDatabase.getDatabase(application).projetoDao()

    val projetos: Flow<List<Projeto>> = dao.projetos
}