package com.github.joaophi.integrador_ix.database

import android.content.Context
import androidx.room.*
import com.github.joaophi.integrador_ix.database.dao.ProjetoDao
import com.github.joaophi.integrador_ix.database.model.Projeto
import com.github.joaophi.integrador_ix.database.model.Requisito
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
    ],
    version = 1,
)
@TypeConverters(Converters::class)
abstract class IntegradorIXDatabase : RoomDatabase() {
    abstract fun projetoDao(): ProjetoDao

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

