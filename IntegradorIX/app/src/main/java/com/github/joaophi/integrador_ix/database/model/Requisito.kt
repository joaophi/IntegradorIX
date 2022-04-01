package com.github.joaophi.integrador_ix.database.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import kotlinx.parcelize.Parcelize
import java.sql.Types
import java.time.LocalDate

@Parcelize
@Entity(
    tableName = "requisito",
    primaryKeys = ["id_projeto", "id"],
    foreignKeys = [ForeignKey(
        entity = Projeto::class,
        parentColumns = ["id"],
        childColumns = ["id_projeto"],
    )],
)
data class Requisito(
    @ColumnInfo(name = "id_projeto")
    val idProjeto: Int,

    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "descricao")
    val descricao: String,

    @ColumnInfo(name = "criacao")
    val criacao: LocalDate,

    @ColumnInfo(name = "importancia")
    val importancia: Int,

    @ColumnInfo(name = "dificuldade")
    val dificuldade: Int,

    @ColumnInfo(name = "horas_para_conclucao")
    val horasParaConclucao: Int,

    @ColumnInfo(name = "latitude")
    val latitude: Double?,

    @ColumnInfo(name = "longitude")
    val longitude: Double?,

    @ColumnInfo(name = "foto1")
    val foto1: String?,

    @ColumnInfo(name = "foto2")
    val foto2: String?,
) : Parcelable
