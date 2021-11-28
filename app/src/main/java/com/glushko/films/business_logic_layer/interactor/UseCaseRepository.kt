package com.glushko.films.business_logic_layer.interactor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.glushko.films.App
import com.glushko.films.R
import com.glushko.films.business_logic_layer.domain.AboutFilm
import com.glushko.films.business_logic_layer.domain.FavoriteFilm
import com.glushko.films.data_layer.datasource.NetworkService
import com.glushko.films.data_layer.datasource.response.ResponseFilm
import kotlinx.coroutines.delay
import retrofit2.awaitResponse

class UseCaseRepository {

    private val dao = App.instance.db.filmsDao()

    suspend fun getFilm(page: Int, liveData: MutableLiveData<ResponseFilm>) {
        /*
        //пока грузятся данные взять из бд
        val list = App.instance.db.filmsDao().getFilms(page)
        //Передать LiveData
        liveData.postValue(ResponseFilm(13, true, isUpdateDB = true, films = list))
        //обновить данные
        println("Загрузка данных")
        delay(10000L)*/
        try {
            val response = NetworkService.makeNetworkService().getFilm(page).awaitResponse()
            if(response.isSuccessful){
                liveData.postValue(response.body()?.apply {
                    isSuccess = true
                    isUpdateDB = false
                })
                insertDB(response.body()?.films, page)
            }else{
                liveData.postValue(ResponseFilm(pagesCount = 0, isSuccess = false, isUpdateDB = false))
            }
        }catch (e: Exception){
            liveData.postValue(ResponseFilm(pagesCount = 0, isSuccess = false, isUpdateDB = false))
        }
    }

    suspend fun getAllFilmsCash(): List<AboutFilm>{
        return App.instance.db.filmsDao().getAllFilms()
    }
    private suspend  fun insertDB(films: List<AboutFilm>?, page: Int) {
        films?.let{

            it.forEachIndexed {index, film ->
                film.comment = ""
                film.imgLike = R.drawable.ic_not_like
                film.position = it.size * (page - 1) + index + 1
            }
            App.instance.db.filmsDao().insertFilms(films)
        }
        println("кол-во фильтмов в бд = ${App.instance.db.filmsDao().getCntFilm()}")
    }

    suspend fun addFavoriteFilm(film: FavoriteFilm){
        dao.insertFavoriteFilm(film)
    }
    suspend fun deleteFavoriteFilm(film: FavoriteFilm){
        dao.deleteFavoriteFilm(film)
    }
    suspend fun getFavoriteFilms(liveData: MutableLiveData<List<FavoriteFilm>>){
        val list = dao.getFavoriteFilms()
        liveData.postValue(list)
    }

}