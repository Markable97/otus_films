package com.glushko.films.data_layer.datasource

import com.glushko.films.data_layer.datasource.response.ResponseFilm
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    companion object{
        //Methods
        const val GET_FILMS_TOP = "films/top"
    }

    @GET(GET_FILMS_TOP)
    fun getFilm(@Query("page")page: Int): Call<ResponseFilm>


}