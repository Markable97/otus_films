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
        println("Загрузка данных страница = $page")
        //пока грузятся данные взять из бд
        val list = App.instance.db.filmsDao().getFilms(page, ResponseFilm.PAGE_COUNT)
        //Передать LiveData
        liveData.postValue(ResponseFilm(list.size, true, isUpdateDB = true, films = list, page = page, err = ResponseFilm.ERROR_NO))
        //обновить данные

        //delay(10000L)
        try {
            val response = NetworkService.makeNetworkService().getFilm(page).awaitResponse()
            if(response.isSuccessful){
                liveData.postValue(response.body()?.apply {
                    isSuccess = true
                    isUpdateDB = false
                    this.page = page
                    err = ResponseFilm.ERROR_NO
                })
                insertDB(response.body()?.films, page)
            }else{
                val error = when(response.code()){
                    401 -> ResponseFilm.ERROR_SERVER_TOKEN
                    429 -> ResponseFilm.ERROR_SERVER_TIME_LIMIT
                    else -> ResponseFilm.ERROR_UNKNOWN
                }
                liveData.postValue(ResponseFilm(pagesCount = 0, isSuccess = false, isUpdateDB = false, page = page, err = error))
            }
        }catch (e: Exception){
            liveData.postValue(ResponseFilm(pagesCount = 0, isSuccess = false, isUpdateDB = false, page = page, err = ResponseFilm.ERROR_NETWORK))
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