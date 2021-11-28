package com.glushko.films.data_layer.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.glushko.films.business_logic_layer.domain.AboutFilm
import com.glushko.films.business_logic_layer.domain.FavoriteFilm

@Database(
    entities = [AboutFilm::class, FavoriteFilm::class],
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


@Dao
interface FilmsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilms(films: List<AboutFilm>)

    @Query("select * from films_table f  order by position  limit 20 offset 20*(:page-1)")
    suspend fun getFilms(page: Int): List<AboutFilm>
    @Query("select count(1) from films_table")
    suspend fun getCntFilm(): Int

    @Query("select * from films_table order by position")
    suspend fun getAllFilms(): List<AboutFilm>

    @Query("select * from films_table f limit 13 offset 0")
    fun getFilmsStart(): LiveData<List<AboutFilm>>
    //@Query("select * from favorite_films_table")
    //suspend fun getFavoriteFilms(): LiveData<List<FavoriteFilm>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteFilm(film: FavoriteFilm)

    @Delete
    suspend fun deleteFavoriteFilm(film: FavoriteFilm)
}