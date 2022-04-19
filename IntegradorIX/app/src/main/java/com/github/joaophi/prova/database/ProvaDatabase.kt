package com.github.joaophi.prova.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.github.joaophi.prova.database.dao.MusicaDao
import com.github.joaophi.prova.database.model.Musica


@Database(
    entities = [Musica::class],
    version = 1
)
abstract class ProvaDatabase : RoomDatabase() {
    abstract fun musicaDao(): MusicaDao

    companion object {
        private lateinit var DATABASE: ProvaDatabase
        fun getDatabase(context: Context): ProvaDatabase {
            if (!::DATABASE.isInitialized)
                DATABASE = Room
                    .databaseBuilder(
                        context.applicationContext,
                        ProvaDatabase::class.java,
                        "prova.db"
                    )
                    .build()
            return DATABASE
        }
    }
}

