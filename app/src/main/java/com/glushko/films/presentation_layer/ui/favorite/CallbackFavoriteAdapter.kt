package com.glushko.films.presentation_layer.ui.favorite

import com.glushko.films.business_logic_layer.domain.FavoriteFilm

interface CallbackFavoriteAdapter {
    fun onDeleteSwipe(film: FavoriteFilm, position: Int)
}