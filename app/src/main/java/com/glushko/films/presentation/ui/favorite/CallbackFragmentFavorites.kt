package com.glushko.films.presentation.ui.favorite

import com.glushko.films.domain.models.FavoriteFilm

interface CallbackFragmentFavorites {
    fun actionInFavoriteMovies(film: FavoriteFilm, isDelete: Boolean)
}