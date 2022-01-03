package com.glushko.films.presentation_layer.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glushko.films.business_logic_layer.domain.AboutFilm
import com.glushko.films.business_logic_layer.domain.FavoriteFilm
import com.glushko.films.business_logic_layer.interactor.UseCaseRepository
import com.glushko.films.data_layer.datasource.response.ResponseFilm
import com.glushko.films.data_layer.datasource.response.ResponseOnceFilm
import kotlinx.coroutines.*

class ViewModelFilms: ViewModel() {
    private val useCase = UseCaseRepository()
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

    fun getFilm(id: Int){
        viewModelScope.launch {
            useCase.getFilmWithId(id, _liveDataOnceFilm)
        }
    }

    fun getFilms(page:Int = 0, isNoAddPage: Boolean = false){
        viewModelScope.launch {
            val curPage = if(page > 0) {
                _page = page
                page
            }else{
                if(isNoAddPage){
                    _page
                }else{
                    ++_page
                }
            }

            useCase.getFilm(curPage, _liveDataFilm)
        }

    }

    fun addFavoriteFilm(film: FavoriteFilm){
        viewModelScope.launch {
            useCase.addFavoriteFilm(film)
        }
    }
    fun deleteFavoriteFilm(film: FavoriteFilm){
        viewModelScope.launch {
            useCase.deleteFavoriteFilm(film)
        }
    }
    fun getFavoriteFilms(){
        viewModelScope.launch {
            useCase.getFavoriteFilms(_liveDataFavoriteFilms)
        }
    }
    fun addComment(film: AboutFilm){
        viewModelScope.launch {
            useCase.addComment(film)
        }
    }
    fun cancelDownloading(){
       viewModelScope.cancel()

    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}