package com.github.joaophi.integrador_ix.database

import android.content.Context
import androidx.room.*
import com.github.joaophi.integrador_ix.database.dao.ProjetoDao
import com.github.joaophi.integrador_ix.database.dao.UsuarioDao
import com.github.joaophi.integrador_ix.database.model.Projeto
import com.github.joaophi.integrador_ix.database.model.Requisito
import com.github.joaophi.integrador_ix.database.model.Usuario
import java.time.LocalDate

class Converters {
    @TypeConverter
    fun fromEpochDay(epochDay: Long): LocalDate = LocalDate.ofEpochDay(epochDay)

    @TypeConverter
    fun toEpochDay(localDate: LocalDate): Long = localDate.toEpochDay()
}

@Database(
    entities = [
        Projeto::class,
        Requisito::class,
        Usuario::class,
    ],
    version = 3,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
    ],
)
@TypeConverters(Converters::class)
abstract class IntegradorIXDatabase : RoomDatabase() {
    abstract fun projetoDao(): ProjetoDao
    abstract fun usuarioDao(): UsuarioDao

    companion object {
        private lateinit var DATABASE: IntegradorIXDatabase
        fun getDatabase(context: Context): IntegradorIXDatabase {
            if (!::DATABASE.isInitialized)
                DATABASE = Room
                    .databaseBuilder(
                        context.applicationContext,
                        IntegradorIXDatabase::class.java,
                        "integradorix.db"
                    )
                    .build()
            return DATABASE
        }
    }
}

