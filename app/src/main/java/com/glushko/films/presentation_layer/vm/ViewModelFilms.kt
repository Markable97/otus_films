package com.glushko.films.presentation_layer.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glushko.films.business_logic_layer.domain.AboutFilm
import com.glushko.films.business_logic_layer.domain.FavoriteFilm
import com.glushko.films.business_logic_layer.interactor.UseCaseRepository
import com.glushko.films.data_layer.datasource.response.ResponseFilm
import com.glushko.films.data_layer.datasource.response.ResponseOnceFilm
import com.glushko.films.data_layer.utils.LoggingHelper
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch

class ViewModelFilms constructor(private val useCase: UseCaseRepository) : ViewModel() {

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
            useCase.getFilmWithId(id, _liveDataOnceFilm)
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
        compositeDisposable.add(useCase.getFilm(curPage, _liveDataFilm))
    }

    fun addFavoriteFilm(film: FavoriteFilm) {
        compositeDisposable.add(useCase.addFavoriteFilm(film))
    }

    fun deleteFavoriteFilm(film: FavoriteFilm) {
        compositeDisposable.add(useCase.deleteFavoriteFilm(film))
    }

    fun getFavoriteFilms() {
        compositeDisposable.add(useCase.getFavoriteFilms(_liveDataFavoriteFilms))
    }

    fun addComment(film: AboutFilm) {
        compositeDisposable.add(useCase.addComment(film))
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
            compositeDisposable.add(useCase.searchFilm(text, _liveDataFilm))
        }

    }
}