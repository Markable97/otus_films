package com.glushko.films.data_layer.datasource.response

import com.glushko.films.business_logic_layer.domain.AboutFilm

data class ResponseFilm(
    val pagesCount: Int,
    var isSuccess: Boolean,
    var films: List<AboutFilm> = listOf()
)
