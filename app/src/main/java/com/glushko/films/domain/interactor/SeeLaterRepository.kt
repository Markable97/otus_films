package com.glushko.films.domain.interactor

import com.glushko.films.domain.models.SeeLaterFilm
import io.reactivex.Single

interface SeeLaterRepository {

    fun getSeeLaterFilms(): Single<List<SeeLaterFilm>>

    fun addSeeLaterFilm(film: SeeLaterFilm)
}