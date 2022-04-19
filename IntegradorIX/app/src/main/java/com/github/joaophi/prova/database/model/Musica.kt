package com.github.joaophi.prova.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "musica")
data class Musica(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "nome")
    val nome: String,

    @ColumnInfo(name = "artista")
    val artista: String,

    @ColumnInfo(name = "album")
    val album: String,

    @ColumnInfo(name = "ano")
    val ano: Long,
)
