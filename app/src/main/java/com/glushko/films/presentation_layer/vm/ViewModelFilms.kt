package com.glushko.films.presentation_layer.vm

import androidx.annotation.RestrictTo
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glushko.films.business_logic_layer.interactor.UseCaseRepository
import com.glushko.films.data_layer.datasource.response.ResponseFilm
import kotlinx.coroutines.*

class ViewModelFilms: ViewModel() {
    var job: Job = Job()
    private val useCase = UseCaseRepository()
    private var _liveDataFilm: MutableLiveData<ResponseFilm> = MutableLiveData()
    val liveDataFilm: LiveData<ResponseFilm> = _liveDataFilm

    fun getFilms(page:Int = 1){
        viewModelScope.launch {
            useCase.getFilm(page, _liveDataFilm)
        }

    }

    fun cancelDownloading(){
        println("ViewModelScope  -  ${viewModelScope.isActive}")
        println("ViewModelScope Context  -  ${viewModelScope.coroutineContext.isActive}")
        println("ViewModelScope Job - ${job.isActive}")
        if(job.isActive){
            job.cancel()
        }
        println("ViewModelScope Job - ${job.isActive}")
        println("ViewModelScope  Context-  ${viewModelScope.coroutineContext.isActive}")
        println("ViewModelScope  -  ${viewModelScope.isActive}")

    }

    override fun onCleared() {
        super.onCleared()
        if(job.isActive){
            job.cancel()
        }
    }

}