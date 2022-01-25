package com.glushko.films.business_logic_layer.interactor

import androidx.lifecycle.MutableLiveData
import com.glushko.films.business_logic_layer.domain.SeeLaterFilm
import com.glushko.films.data_layer.repository.FilmsDao
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SeeLaterRepository @Inject constructor(private val dao: FilmsDao){

    fun getSeeLaterFilms(liveDataSeeLater: MutableLiveData<List<SeeLaterFilm>>): Disposable {
        return dao.getSeeLaterFilms()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ filmsSeeLater ->
                liveDataSeeLater.postValue(filmsSeeLater)
            }
    }

    fun addSeeLaterFilm(film: SeeLaterFilm): Disposable {
        return  Single.just("")
            .subscribeOn(Schedulers.io())
            .map {dao.addSeeLaterFilm(film)}
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }


}