package com.glushko.films.presentation_layer.ui.favorite

import com.glushko.films.business_logic_layer.domain.FavoriteFilm

interface CallbackFragmentFavorites {
    fun actionInFavoriteMovies(film: FavoriteFilm, isDelete: Boolean)
}