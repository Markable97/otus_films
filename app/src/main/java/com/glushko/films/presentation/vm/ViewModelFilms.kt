package com.glushko.films.presentation.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glushko.films.domain.models.AboutFilm
import com.glushko.films.domain.models.FavoriteFilm
import com.glushko.films.domain.interactor.FilmsRepository
import com.glushko.films.data.datasource.response.ResponseFilm
import com.glushko.films.data.datasource.response.ResponseOnceFilm
import com.glushko.films.data.utils.LoggingHelper
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch

class ViewModelFilms constructor(private val filmsRepository: FilmsRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    //private val useCase = UseCaseRepository()
    private var _page: Int = 0
    private var _liveDataFilm: MutableLiveData<ResponseFilm> = MutableLiveData()
    val liveDataFilm: LiveData<ResponseFilm> = _liveDataFilm
    private val _liveDataFavoriteFilms: MutableLiveData<List<FavoriteFilm>> = MutableLiveData()
    val liveDataFavoriteFilms: LiveData<List<FavoriteFilm>> = _liveDataFavoriteFilms
    private val _liveDataOnceFilm: MutableLiveData<ResponseOnceFilm> = MutableLiveData()
    val liveDataOnceFilm: LiveData<ResponseOnceFilm> = _liveDataOnceFilm
    fun clearFilms() {
        _liveDataFilm.value?.films = listOf()
    }

    fun getFilm(id: Int) {
        viewModelScope.launch {
            filmsRepository.getFilmWithId(id, _liveDataOnceFilm)
        }
    }

    fun getFilms(page: Int = 0, isNoAddPage: Boolean = false) {
        val curPage = if (page > 0) {
            _page = page
            page
        } else {
            if (isNoAddPage) {
                _page
            } else {
                ++_page
            }
        }
        compositeDisposable.add(filmsRepository.getFilm(curPage, _liveDataFilm))
    }

    fun addFavoriteFilm(film: FavoriteFilm) {
        compositeDisposable.add(filmsRepository.addFavoriteFilm(film))
    }

    fun deleteFavoriteFilm(film: FavoriteFilm) {
        compositeDisposable.add(filmsRepository.deleteFavoriteFilm(film))
    }

    fun getFavoriteFilms() {
        compositeDisposable.add(filmsRepository.getFavoriteFilms(_liveDataFavoriteFilms))
    }

    fun addComment(film: AboutFilm) {
        compositeDisposable.add(filmsRepository.addComment(film))
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun searchFilm(text: String?) {
        if (text.isNullOrEmpty()) {
            LoggingHelper.log(Log.DEBUG, "Пустое значение подтянем фильмы как всегда")
            getFilms(page = 1)
        } else {
            compositeDisposable.add(filmsRepository.searchFilm(text, _liveDataFilm))
        }

    }
}