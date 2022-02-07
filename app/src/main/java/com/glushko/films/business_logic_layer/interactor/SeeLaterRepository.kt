package com.glushko.films.business_logic_layer.interactor

import androidx.lifecycle.MutableLiveData
import com.glushko.films.business_logic_layer.domain.SeeLaterFilm
import com.glushko.films.data_layer.repository.FilmsDao
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

open class SeeLaterRepository @Inject constructor(private val dao: FilmsDao){

    fun getSeeLaterFilms(): Single<List<SeeLaterFilm>> {
        return dao.getSeeLaterFilms()
    }

    fun addSeeLaterFilm(film: SeeLaterFilm) {
        return dao.addSeeLaterFilm(film)
    }


}