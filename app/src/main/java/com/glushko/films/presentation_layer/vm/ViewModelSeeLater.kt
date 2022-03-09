package com.glushko.films.presentation_layer.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.glushko.films.business_logic_layer.domain.SeeLaterFilm
import com.glushko.films.business_logic_layer.interactor.SeeLaterRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ViewModelSeeLater constructor(private val useCase: SeeLaterRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _liveDataSeeLater = MutableLiveData<List<SeeLaterFilm>>()
    val liveDataSeeLater: LiveData<List<SeeLaterFilm>> = _liveDataSeeLater
    private val _liveDataAddSeeLaterFilm = MutableLiveData<Boolean>()
    val liveDataAddSeeLaterFilm: LiveData<Boolean> = _liveDataAddSeeLaterFilm


    fun getSeeLaterFilms() {
        compositeDisposable.add(useCase.getSeeLaterFilms()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ filmsSeeLater ->
                _liveDataSeeLater.postValue(filmsSeeLater)
            }, {
                it.printStackTrace()
            })
        )
    }

    fun addSeeLaterFilm(film: SeeLaterFilm) {
//        LoggingHelper.log(Log.DEBUG, "Добавление фильма в список посмотреть позже")
        compositeDisposable.add(
            Single.just("")
                .subscribeOn(Schedulers.io())
                .map { useCase.addSeeLaterFilm(film) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _liveDataAddSeeLaterFilm.value = true
                }, {
                    _liveDataAddSeeLaterFilm.value = false
                })
        )

    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}