package com.glushko.films.presentation_layer.ui.see_later

import com.glushko.films.business_logic_layer.domain.SeeLaterFilm

interface CallbackAdapterSeeLater {
    fun onClickEdit(film: SeeLaterFilm, position: Int)
}