package com.glushko.films.presentation.ui.films

import com.glushko.films.domain.models.AboutFilm

interface CallbackAdapterFilms {
    fun onClickDetail(film: AboutFilm, position: Int)
    fun onClickLike(film: AboutFilm, position: Int)
}