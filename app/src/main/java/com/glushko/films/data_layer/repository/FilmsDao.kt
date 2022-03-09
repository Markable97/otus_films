package com.glushko.films.data_layer.repository

import androidx.lifecycle.LiveData
import androidx.room.*
import com.glushko.films.business_logic_layer.domain.AboutFilm
import com.glushko.films.business_logic_layer.domain.FavoriteFilm
import com.glushko.films.business_logic_layer.domain.SeeLaterFilm
import com.glushko.films.business_logic_layer.domain.UpdateTime
import io.reactivex.Single

@Dao
interface FilmsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFilms(films: List<AboutFilm>)

    @Query(
        """
        select f.id, 
               f.name, 
               f.comment, 
               case when ff.id is not null 
                    then 1 
                    else  0 
               end  as `like`, 
               f.img, 
               f.imgLike, 
               f.position,
               f.typeList
        from films_table f 
        left join favorite_films_table ff on f.id = ff.id  
        where f.name like '%'||:text||'%'
    """
    )
    fun searchFilm(text: String): Single<List<AboutFilm>>

    @Query(
        """
        select f.id, 
               f.name, 
               f.comment, 
               case when ff.id is not null 
                    then 1 
                    else  0 
               end  as `like`, 
               f.img, 
               f.imgLike, 
               f.position,
               f.typeList
        from films_table f 
        left join favorite_films_table ff on f.id = ff.id  
        where f.id = :id 
    """
    )
    fun getFilm(id: Int): AboutFilm

    @Query(
        """
        select f.id, 
               f.name, 
               f.comment, 
               case when ff.id is not null 
                    then 1 
                    else  0 
               end  as `like`, 
               f.img, 
               f.imgLike, 
               f.position,
               f.typeList
        from films_table f
        left join favorite_films_table ff on f.id = ff.id
        where f.typeList = :type
        order by position  limit :count offset :count*(:page-1)
        """
    )
    fun getFilms(page: Int, count: Int, type: String): Single<List<AboutFilm>>

    @Query("select count(1) from films_table")
    suspend fun getCntFilm(): Int

    @Query("select * from films_table order by position")
    suspend fun getAllFilms(): List<AboutFilm>

    @Query("select * from films_table f limit 13 offset 0")
    fun getFilmsStart(): LiveData<List<AboutFilm>>

    @Query("select * from favorite_films_table")
    fun getFavoriteFilms(): Single<List<FavoriteFilm>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteFilm(film: FavoriteFilm)

    @Delete
    fun deleteFavoriteFilm(film: FavoriteFilm): Single<Int>

    @Query("select count(1) from favorite_films_table t where t.id = :filmID")
    fun isInFavorite(filmID: Int): Int

    @Update
    fun addComment(film: AboutFilm): Single<Int>

    @Query("select comment from films_table where id = :id")
    fun getCommentForFilm(id: Int): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTimeRefresh(time: UpdateTime)

    @Query("select t.time from refresh_table t")
    fun getRefreshTime(): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addSeeLaterFilm(film: SeeLaterFilm)

    @Query("select * from see_later_films_table")
    fun getSeeLaterFilms(): Single<List<SeeLaterFilm>>

    @Query("select * from see_later_films_table where id = :id")
    fun getSeeLaterFilm(id: Int): Single<SeeLaterFilm>

}