package com.glushko.films.presentation_layer.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glushko.films.business_logic_layer.domain.AboutFilm
import com.glushko.films.business_logic_layer.domain.FavoriteFilm
import com.glushko.films.business_logic_layer.interactor.UseCaseRepository
import com.glushko.films.data_layer.datasource.response.ResponseFilm
import kotlinx.coroutines.*

class ViewModelFilms: ViewModel() {
    private val useCase = UseCaseRepository()
    private var _page: Int = 0
    private var _liveDataFilm: MutableLiveData<ResponseFilm> = MutableLiveData()
    val liveDataFilm: LiveData<ResponseFilm> = _liveDataFilm
    private val _liveDataFavoriteFilms: MutableLiveData<List<FavoriteFilm>> = MutableLiveData()
    val liveDataFavoriteFilms: LiveData<List<FavoriteFilm>> = _liveDataFavoriteFilms
    fun clearFilms() {
        _liveDataFilm.value?.films = listOf()
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
        println("ViewModelScope  -  ${viewModelScope.isActive}")
        println("ViewModelScope Context  -  ${viewModelScope.coroutineContext.isActive}")
       viewModelScope.cancel()
        println("ViewModelScope  Context-  ${viewModelScope.coroutineContext.isActive}")
        println("ViewModelScope  -  ${viewModelScope.isActive}")

    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
        println("Очистка ViewModel")
    }
}