package com.glushko.films.data.datasource.response

import com.glushko.films.domain.models.AboutFilm

data class ResponseOnceFilm(
    val err: Int,
    val film: AboutFilm?
)