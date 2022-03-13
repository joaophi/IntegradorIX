package com.github.joaophi.integrador_ix.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.github.joaophi.integrador_ix.database.model.Projeto
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ProjetoDao {
    @get:Query(
        value = """
            SELECT *
            FROM projeto
        """
    )
    abstract val projetos: Flow<List<Projeto>>
}