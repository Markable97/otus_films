package com.glushko.films.presentation.ui.see_later

import com.glushko.films.domain.models.SeeLaterFilm

interface CallbackAdapterSeeLater {
    fun onClickEdit(film: SeeLaterFilm, position: Int)
}