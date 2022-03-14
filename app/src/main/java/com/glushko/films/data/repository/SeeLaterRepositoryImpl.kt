package com.glushko.films.data.repository

import com.glushko.films.domain.models.SeeLaterFilm
import com.glushko.films.data.database.FilmsDao
import com.glushko.films.domain.interactor.SeeLaterRepository
import io.reactivex.Single
import javax.inject.Inject

open class SeeLaterRepositoryImpl @Inject constructor(private val dao: FilmsDao):
    SeeLaterRepository {

    override fun getSeeLaterFilms(): Single<List<SeeLaterFilm>> {
        return dao.getSeeLaterFilms()
    }

    override fun addSeeLaterFilm(film: SeeLaterFilm) {
        return dao.addSeeLaterFilm(film)
    }


}