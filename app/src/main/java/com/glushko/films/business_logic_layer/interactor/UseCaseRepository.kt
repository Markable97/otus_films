package com.glushko.films.business_logic_layer.interactor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.glushko.films.App
import com.glushko.films.R
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
        val list = App.instance.db.filmsDao().getFilms(page)
        //Передать LiveData
        liveData.postValue(ResponseFilm(13, true, isUpdateDB = true, films = list))
        //обновить данные
        /*val response = NetworkService.makeNetworkService().getFilm(page).awaitResponse()
        if(response.isSuccessful){
            liveData.postValue(response.body()?.apply {
                isSuccess = true
                isUpdateDB = false
            })
            insertDB(response.body()?.films, page)
        }else{
            liveData.postValue(ResponseFilm(pagesCount = 0, isSuccess = false, isUpdateDB = false))
        }*/
    }

    private suspend  fun insertDB(films: List<AboutFilm>?, page: Int) {
        films?.let{

            it.forEachIndexed {index, film ->
                film.comment = ""
                film.imgLike = R.drawable.ic_not_like
                film.position = 13 * (page - 1) + index + 1
            }
            App.instance.db.filmsDao().insertFilms(films)
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