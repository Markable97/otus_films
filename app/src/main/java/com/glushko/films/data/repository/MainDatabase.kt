package com.glushko.films.data.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.glushko.films.domain.models.AboutFilm
import com.glushko.films.domain.models.FavoriteFilm
import com.glushko.films.domain.models.SeeLaterFilm
import com.glushko.films.domain.models.UpdateTime

@Database(
    entities = [AboutFilm::class, FavoriteFilm::class, UpdateTime::class, SeeLaterFilm::class],
    version = 1, exportSchema = false
)
abstract class MainDatabase : RoomDatabase() {
    abstract fun filmsDao(): FilmsDao

    companion object {
        private var instanceDB: MainDatabase? = null
        fun getDatabase(context: Context): MainDatabase {
            if (instanceDB == null) {
                synchronized(this) {
                    instanceDB = Room.databaseBuilder(
                        context.applicationContext,
                        MainDatabase::class.java,
                        "films_db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return instanceDB!!
        }
    }
}