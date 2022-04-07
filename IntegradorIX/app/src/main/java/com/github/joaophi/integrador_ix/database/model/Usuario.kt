package com.github.joaophi.integrador_ix.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "usuario", primaryKeys = ["login"])
data class Usuario(
    @ColumnInfo(name = "login")
    val login: String,

    @ColumnInfo(name = "senha")
    val senha: String,
)
