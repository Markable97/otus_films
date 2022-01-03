package com.glushko.films.data_layer.repository

import androidx.lifecycle.LiveData
import androidx.room.*
import com.glushko.films.business_logic_layer.domain.AboutFilm
import com.glushko.films.business_logic_layer.domain.FavoriteFilm
import com.glushko.films.business_logic_layer.domain.UpdateTime

@Dao
interface FilmsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilms(films: List<AboutFilm>)

    @Query("""
        select f.id, 
               f.name, 
               f.comment, 
               case when ff.id is not null 
                    then 1 
                    else  0 
               end  as `like`, 
               f.img, 
               f.imgLike, 
               f.position 
        from films_table f 
        left join favorite_films_table ff on f.id = ff.id  
        where f.id = :id 
    """)
    suspend fun getFilm(id: Int): AboutFilm

    @Query("""
        select f.id, 
               f.name, 
               f.comment, 
               case when ff.id is not null 
                    then 1 
                    else  0 
               end  as `like`, 
               f.img, 
               f.imgLike, 
               f.position 
        from films_table f
        left join favorite_films_table ff on f.id = ff.id
        order by position  limit :count offset :count*(:page-1)
        """ )
    suspend fun getFilms(page: Int, count: Int): List<AboutFilm>
    @Query("select count(1) from films_table")
    suspend fun getCntFilm(): Int

    @Query("select * from films_table order by position")
    suspend fun getAllFilms(): List<AboutFilm>

    @Query("select * from films_table f limit 13 offset 0")
    fun getFilmsStart(): LiveData<List<AboutFilm>>
    @Query("select * from favorite_films_table")
    suspend fun getFavoriteFilms(): List<FavoriteFilm>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteFilm(film: FavoriteFilm)

    @Delete
    suspend fun deleteFavoriteFilm(film: FavoriteFilm)

    @Query("select count(1) from favorite_films_table t where t.id = :filmID")
    suspend fun isInFavorite(filmID: Int): Int

    @Update
    suspend fun addComment(film: AboutFilm): Int
    @Query("select comment from films_table where id = :id")
    suspend fun getCommentForFilm(id: Int): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTimeRefresh(time: UpdateTime)

    @Query("select t.time from refresh_table t")
    suspend fun getRefreshTime(): Long?

}