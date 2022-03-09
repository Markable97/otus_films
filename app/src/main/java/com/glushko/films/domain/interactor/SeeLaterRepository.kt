package com.glushko.films.domain.interactor

import com.glushko.films.domain.models.SeeLaterFilm
import com.glushko.films.data.repository.FilmsDao
import io.reactivex.Single
import javax.inject.Inject

open class SeeLaterRepository @Inject constructor(private val dao: FilmsDao) {

    fun getSeeLaterFilms(): Single<List<SeeLaterFilm>> {
        return dao.getSeeLaterFilms()
    }

    fun addSeeLaterFilm(film: SeeLaterFilm) {
        return dao.addSeeLaterFilm(film)
    }


}