package com.glushko.films.data.datasource

import com.glushko.films.domain.models.AboutOnceFilm
import com.glushko.films.data.datasource.response.ResponseFilm
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {
    companion object {
        //Methods
        const val GET_FILMS_TOP = "films/top"
        const val GET_FILMS = "films/"
    }

    @GET(GET_FILMS_TOP)
    fun getFilm(@Query("type") type: String, @Query("page") page: Int): Single<ResponseFilm>

    @GET(GET_FILMS_TOP)
    fun getFilmRx(@Query("type") type: String, @Query("page") page: Int): Single<ResponseFilm>

    @GET("")
    fun getFilmWithID(@Url id: String): Single<AboutOnceFilm>

}