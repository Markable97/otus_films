package com.glushko.films.data_layer.datasource

import com.glushko.films.business_logic_layer.domain.AboutOnceFilm
import com.glushko.films.data_layer.datasource.response.ResponseFilm
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {
    companion object{
        //Methods
        const val GET_FILMS_TOP = "films/top"
        const val GET_FILMS = "films/"
    }

    @GET(GET_FILMS_TOP)
    fun getFilm(@Query("type") type:String, @Query("page")page: Int): Call<ResponseFilm>

    @GET(GET_FILMS_TOP)
    fun getFilmRx(@Query("type") type:String, @Query("page")page: Int): Single<ResponseFilm>

    @GET("")
    fun getFilmWithID(@Url id: String):Call<AboutOnceFilm>

}