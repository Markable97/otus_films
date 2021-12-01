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
        val list = dao.getFilms(page, ResponseFilm.PAGE_COUNT)
        //Передать LiveData
        liveData.postValue(ResponseFilm(list.size, true, isUpdateDB = true, films = list, page = page, err = ResponseFilm.ERROR_NO))
        //обновить данные

        //delay(10000L)
        try {
            val response = NetworkService.makeNetworkService().getFilm(page).awaitResponse()
            if(response.isSuccessful){
                val filmsFromServer = response.body()?.films
                filmsFromServer?.forEach {
                    if(isInFavorite(it.id) == 1){
                        it.like = 1
                        it.imgLike = R.drawable.ic_like
                    }else{
                        it.like = 0
                        it.imgLike = R.drawable.ic_not_like
                    }
                    it.comment = getComment(it.id)
                }

                liveData.postValue(response.body()?.apply {
                    this.films = filmsFromServer?: listOf()
                    isSuccess = true
                    isUpdateDB = false
                    this.page = page
                    err = ResponseFilm.ERROR_NO
                })
                insertDB(filmsFromServer, page)
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
                film.position = it.size * (page - 1) + index + 1
            }
            App.instance.db.filmsDao().insertFilms(films)
        }
        println("кол-во фильтмов в бд = ${App.instance.db.filmsDao().getCntFilm()}")
    }

    private suspend fun isInFavorite(id: Int): Int{
        return dao.isInFavorite(id)
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

    private suspend fun getComment(id: Int): String{
        val str = dao.getCommentForFilm(id)
        println("получаем коммент $id $str")
        return dao.getCommentForFilm(id)
    }
    suspend fun addComment(film: AboutFilm) {
        val update = dao.addComment(film)
        println("Обновилось? $update")
    }

}