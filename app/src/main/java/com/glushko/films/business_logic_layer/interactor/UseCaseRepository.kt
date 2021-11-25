package com.glushko.films.business_logic_layer.interactor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.glushko.films.App
import com.glushko.films.business_logic_layer.domain.AboutFilm
import com.glushko.films.data_layer.datasource.NetworkService
import com.glushko.films.data_layer.datasource.response.ResponseFilm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse

class UseCaseRepository {

    suspend fun getFilm(page: Int, liveData: MutableLiveData<ResponseFilm>) {
        //пока грузятся данные взять из бд
        //val list = App.instance.db.filmsDao().getFilms(page)
        //Передать LiveData
        //liveData.postValue(ResponseFilm(13, true, list.value!!))
        //обновить данные
        val response = NetworkService.makeNetworkService().getFilm(page).awaitResponse()
        if(response.isSuccessful){
            liveData.postValue(response.body()?.apply {
                isSuccess = true
            })
        }else{
            liveData.postValue(ResponseFilm(pagesCount = 0, isSuccess = false))
        }
    }

    suspend fun getNextFilm(page: Int): Collection<AboutFilm> {
        val response = NetworkService.makeNetworkService().getFilm(page).awaitResponse()
        var films: List<AboutFilm> = listOf()
        if(response.isSuccessful){
            films = response.body()?.films?: listOf()
        }
        return films
    }
}