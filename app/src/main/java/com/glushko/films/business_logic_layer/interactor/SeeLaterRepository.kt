package com.glushko.films.business_logic_layer.interactor

import com.glushko.films.App
import com.glushko.films.business_logic_layer.domain.SeeLaterFilm
import com.glushko.films.data_layer.repository.FilmsDao
import javax.inject.Inject

class SeeLaterRepository @Inject constructor(private val dao: FilmsDao){

    suspend fun getSeeLaterFilms(): List<SeeLaterFilm> {
        return dao.getSeeLaterFilms()
    }

    suspend fun addSeeLaterFilm(film: SeeLaterFilm) {
        dao.addSeeLaterFilm(film)
    }


}