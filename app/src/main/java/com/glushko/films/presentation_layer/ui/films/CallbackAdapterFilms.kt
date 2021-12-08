package com.glushko.films.presentation_layer.ui.films

import com.glushko.films.business_logic_layer.domain.AboutFilm

interface CallbackAdapterFilms {
    fun onClickDetail(film: AboutFilm, position: Int)
    fun onClickLike(film: AboutFilm, position: Int)
}