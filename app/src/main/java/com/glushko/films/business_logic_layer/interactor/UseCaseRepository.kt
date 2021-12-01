package com.glushko.films.business_logic_layer.interactor

import androidx.lifecycle.MutableLiveData
import com.glushko.films.App
import com.glushko.films.R
import com.glushko.films.business_logic_layer.domain.AboutFilm
import com.glushko.films.business_logic_layer.domain.FavoriteFilm
import com.glushko.films.business_logic_layer.domain.UpdateTime
import com.glushko.films.data_layer.datasource.NetworkService
import com.glushko.films.data_layer.datasource.response.ResponseFilm
import retrofit2.awaitResponse
import java.util.concurrent.TimeUnit

class UseCaseRepository {

    companion object {
        val FRESH_TIMEOUT = TimeUnit.MINUTES.toMillis(2) //TimeUnit.DAYS.toMillis(1)
    }

    private val dao = App.instance.db.filmsDao()

    suspend fun getFilm(page: Int, liveData: MutableLiveData<ResponseFilm>) {
        println("Загрузка данных страница = $page")
        //пока грузятся данные взять из бд
        val list = dao.getFilms(page, ResponseFilm.PAGE_COUNT)
        //Передать LiveData
        liveData.postValue(ResponseFilm(list.size, true, isUpdateDB = true, films = list, page = page, err = ResponseFilm.ERROR_NO))
        //обновить данные
        refreshFilms(page, liveData, list.isEmpty())
    }

    private suspend fun refreshFilms(page: Int, liveData: MutableLiveData<ResponseFilm>, isEmptyDB: Boolean){
        val currentTime = System.currentTimeMillis()
        val oldTime = dao.getRefreshTime()?:0L
        val isRefresh = (currentTime - oldTime) >=  FRESH_TIMEOUT
        println("isEmptyDB = $isEmptyDB isRefresh = $isRefresh")
        if(isEmptyDB or isRefresh ){
            println("Пора обновить даные")
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
                        it.comment = getComment(it.id)?:""
                    }

                    liveData.postValue(response.body()?.apply {
                        this.films = filmsFromServer?: listOf()
                        isSuccess = true
                        isUpdateDB = false
                        this.page = page
                        err = ResponseFilm.ERROR_NO
                    })
                    insertDB(filmsFromServer, page)
                    dao.addTimeRefresh(UpdateTime(time = System.currentTimeMillis()))
                }else{
                    val error = when(response.code()){
                        401 -> ResponseFilm.ERROR_SERVER_TOKEN
                        429 -> ResponseFilm.ERROR_SERVER_TIME_LIMIT
                        else -> ResponseFilm.ERROR_UNKNOWN
                    }
                    liveData.postValue(ResponseFilm(pagesCount = 0, isSuccess = false, isUpdateDB = false, page = page, err = error))
                }
            }catch (e: Exception){
                e.printStackTrace()
                liveData.postValue(ResponseFilm(pagesCount = 0, isSuccess = false, isUpdateDB = false, page = page, err = ResponseFilm.ERROR_NETWORK))
            }
        }else{
            println("Никакого обновления")
            liveData.postValue(ResponseFilm(pagesCount = 0, isSuccess = true, isUpdateDB = false,  page= page, err = ResponseFilm.ERROR_NO))
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
            dao.insertFilms(films)
        }
        println("кол-во фильтмов в бд = ${dao.getCntFilm()}")
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

    private suspend fun getComment(id: Int): String?{
        return dao.getCommentForFilm(id)
    }
    suspend fun addComment(film: AboutFilm) {
        dao.addComment(film)
    }

}