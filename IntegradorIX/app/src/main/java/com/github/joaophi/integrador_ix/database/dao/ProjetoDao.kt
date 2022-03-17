package com.github.joaophi.integrador_ix.database.dao

import androidx.room.*
import com.github.joaophi.integrador_ix.database.model.Projeto
import com.github.joaophi.integrador_ix.database.model.Requisito
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

    @Query(
        value = """
            SELECT *
            FROM projeto
            WHERE id = :id
        """
    )
    abstract suspend fun getProjeto(id: Int): Projeto?

    @Query(
        value = """
            SELECT *
            FROM requisito
            WHERE id_projeto = :id
        """
    )
    abstract suspend fun getRequisitos(id: Int): List<Requisito>

    @Query(
        value = """
            DELETE FROM requisito
            WHERE id_projeto = :id
        """
    )
    abstract fun deleteRequisitos(id: Int)

    @Query(
        value = """
            DELETE FROM projeto
            WHERE id = :id
        """
    )
    abstract fun deleteProjeto(id: Int)

    @Transaction
    open suspend fun delete(id: Int) {
        deleteRequisitos(id)
        deleteProjeto(id)
    }

    @Query(
        value = """
            SELECT COALESCE(MAX(id), 0) + 1
            FROM projeto
        """
    )
    abstract fun getId(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun upsertProjeto(projeto: Projeto)

    @Insert
    abstract fun insertRequisitos(requisitos: List<Requisito>)

    @Transaction
    open suspend fun save(projeto: Projeto, requisitos: List<Requisito>) {
        val id = when {
            projeto.id > -1 -> projeto.id
            else -> getId()
        }
        upsertProjeto(projeto.copy(id = id))
        deleteRequisitos(projeto.id)
        insertRequisitos(requisitos.map { it.copy(idProjeto = id) })
    }
}