package com.github.joaophi.integrador_ix.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.time.LocalDate

@Entity(tableName = "projeto", primaryKeys = ["id"])
data class Projeto(
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "nome")
    val nome: String,

    @ColumnInfo(name = "inicio")
    val inicio: LocalDate,

    @ColumnInfo(name = "estimativa_conclusao")
    val estimativaConclusao: LocalDate,
)
