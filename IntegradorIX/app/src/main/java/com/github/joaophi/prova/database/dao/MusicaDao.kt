package com.github.joaophi.prova.database.dao

import androidx.room.*
import com.github.joaophi.prova.database.model.Musica
import kotlinx.coroutines.flow.Flow

@Dao
abstract class MusicaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun upsert(item: Musica)

    @Delete
    abstract suspend fun delete(item: Musica)

    @Query(
        value = """
            SELECT *
            FROM musica
            WHERE id = :id
        """
    )
    abstract suspend fun get(id: Long): Musica

    @get:Query(
        value = """
            SELECT *
            FROM musica
        """
    )
    abstract val musicas: Flow<List<Musica>>
}
