package com.glushko.films.data_layer.datasource.response

import com.glushko.films.business_logic_layer.domain.AboutFilm

data class ResponseFilm(
    val pagesCount: Int, //Api не достоверную инфу предоставляет, по факту не 13 элементов, а 20
    var isSuccess: Boolean,
    var isUpdateDB: Boolean,
    var films: List<AboutFilm> = listOf()
)
