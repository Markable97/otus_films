package com.glushko.films.presentation.ui.favorite

import com.glushko.films.domain.models.FavoriteFilm

interface CallbackFavoriteAdapter {
    fun onDeleteSwipe(film: FavoriteFilm, position: Int)
}