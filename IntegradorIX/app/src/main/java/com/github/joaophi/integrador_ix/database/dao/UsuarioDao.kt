package com.github.joaophi.integrador_ix.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.github.joaophi.integrador_ix.database.model.Usuario

@Dao
abstract class UsuarioDao {
    @Insert
    abstract suspend fun insertUsuario(usuario: Usuario)

    @Query(
        value = """
            SELECT *
            FROM usuario
            WHERE login = :login
        """
    )
    abstract suspend fun getUsuario(login: String): Usuario?
}
