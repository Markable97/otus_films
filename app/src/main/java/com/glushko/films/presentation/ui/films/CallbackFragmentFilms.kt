package com.glushko.films.presentation.ui.films

import com.glushko.films.domain.models.AboutFilm

interface CallbackFragmentFilms {
    fun actionWithMovie(position: Int, film: AboutFilm)
    fun showProgressbar()
}