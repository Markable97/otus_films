package com.glushko.films.data_layer.datasource.response

import com.glushko.films.business_logic_layer.domain.AboutFilm

data class ResponseOnceFilm(
    val err: Int,
    val film: AboutFilm?
)