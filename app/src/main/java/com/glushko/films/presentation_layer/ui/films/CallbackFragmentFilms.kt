package com.glushko.films.presentation_layer.ui.films

import com.glushko.films.business_logic_layer.domain.AboutFilm

interface CallbackFragmentFilms {
    fun actionWithMovie(position: Int, film: AboutFilm)
    fun showProgressbar()
}