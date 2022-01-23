package com.glushko.films.business_logic_layer.interactor

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.glushko.films.App
import com.glushko.films.R
import com.glushko.films.business_logic_layer.domain.AboutFilm
import com.glushko.films.business_logic_layer.domain.FavoriteFilm
import com.glushko.films.business_logic_layer.domain.UpdateTime
import com.glushko.films.data_layer.datasource.ApiService.Companion.GET_FILMS
import com.glushko.films.data_layer.datasource.NetworkService
import com.glushko.films.data_layer.datasource.response.ResponseFilm
import com.glushko.films.data_layer.datasource.response.ResponseOnceFilm
import com.glushko.films.data_layer.utils.TYPE_FILM_LIST
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.awaitResponse
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

class UseCaseRepository {

    companion object {
        val FRESH_TIMEOUT = TimeUnit.MINUTES.toMillis(1) //TimeUnit.DAYS.toMillis(1)
    }

    private val dao = App.instance.db.filmsDao()

     fun getFilm(page: Int, liveData: MutableLiveData<ResponseFilm>): Disposable {
        println("Загрузка данных страница = $page")
        //пока грузятся данные взять из бд
         val disposable: Disposable = dao.getFilms(page, ResponseFilm.PAGE_COUNT, TYPE_FILM_LIST)
             .subscribeOn(Schedulers.io())
             .map { list ->
                 liveData.postValue(
                     ResponseFilm(
                         list.size,
                         true,
                         isUpdateDB = true,
                         films = list,
                         page = page,
                         err = ResponseFilm.ERROR_NO
                     )
                 )
                 Pair(dao.getRefreshTime(), list.isEmpty())
             }
             .map {it ->
                 val currentTime = System.currentTimeMillis()
                 val isRefresh = (currentTime - it.first) >=  FRESH_TIMEOUT
                 it.second or isRefresh
             }
             .observeOn(AndroidSchedulers.mainThread())
             .subscribe({
              if(it){
                  getFilmsFromServer(page, liveData)
              }
             }, {
                 it.printStackTrace()
                 liveData.postValue(ResponseFilm(pagesCount = 0, isSuccess = false, isUpdateDB = false, page = page, err = ResponseFilm.ERROR_UNKNOWN))
             })
         return disposable
    }



    private fun getFilmsFromServer(page: Int, liveData: MutableLiveData<ResponseFilm>): Disposable{
        return NetworkService.makeNetworkService().getFilmRx(TYPE_FILM_LIST, page)
            .subscribeOn(Schedulers.io())
            .map(this::mappingFilms)
            .map { insertDB(it, page) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                       liveData.postValue(
                           it.apply {
                               isSuccess = true
                               isUpdateDB = false
                               this.page = page
                               err = ResponseFilm.ERROR_NO
                           })
            }, {
                it.printStackTrace()
                when(it){
                    is HttpException -> {
                        val error = when(it.code()){
                            401 -> ResponseFilm.ERROR_SERVER_TOKEN
                            429 -> ResponseFilm.ERROR_SERVER_TIME_LIMIT
                            else -> ResponseFilm.ERROR_UNKNOWN
                        }
                        liveData.postValue(ResponseFilm(pagesCount = 0, isSuccess = false, isUpdateDB = false, page = page, err = error))
                    }
                    is UnknownHostException -> {
                        liveData.postValue(ResponseFilm(pagesCount = 0, isSuccess = false, isUpdateDB = false, page = page, err = ResponseFilm.ERROR_NETWORK))
                    }
                    else -> liveData.postValue(ResponseFilm(pagesCount = 0, isSuccess = false, isUpdateDB = false, page = page, err = ResponseFilm.ERROR_UNKNOWN))
                }
            })
    }

    private fun mappingFilms(response: ResponseFilm) : ResponseFilm{
        val films = response.films
        films.forEach {
            if(isInFavorite(it.id) == 1){
                it.like = 1
                it.imgLike = R.drawable.ic_like
            }else{
                it.like = 0
                it.imgLike = R.drawable.ic_not_like
            }
            it.comment = getComment(it.id)?:""
            it.typeList = TYPE_FILM_LIST
        }
        response.films = films
        return response
    }
    private fun insertDB(response: ResponseFilm, page: Int): ResponseFilm {
        val films = response.films
        films.let{

            it.forEachIndexed {index, film ->
                film.position = it.size * (page - 1) + index + 1
            }
            dao.insertFilms(films)
        }
        response.films = films
        return response
        //println("кол-во фильтмов в бд = ${dao.getCntFilm()}")
    }

    private fun isInFavorite(id: Int): Int{
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

    private fun getComment(id: Int): String?{
        return dao.getCommentForFilm(id)
    }
    suspend fun addComment(film: AboutFilm) {
        dao.addComment(film)
    }

    suspend fun getFilmWithId(id: Int, liveData: MutableLiveData<ResponseOnceFilm>){
        try {
            val response = NetworkService.makeNetworkService().getFilmWithID("$GET_FILMS$id").awaitResponse()
            if(response.isSuccessful){
                Log.d("TAG", "${response.body()}")
                val filmOnce = response.body()!!
                getFilmWithIdDB(filmOnce.id, liveData)
            }else{
                val error = when(response.code()){
                    401 -> ResponseFilm.ERROR_SERVER_TOKEN
                    429 -> ResponseFilm.ERROR_SERVER_TIME_LIMIT
                    else -> ResponseFilm.ERROR_UNKNOWN
                }
                Log.d("TAG", "$error")
                liveData.postValue(ResponseOnceFilm(error, null))
                getFilmWithIdDB(id, liveData)
            }
        }catch (e: Exception){
            e.printStackTrace()
            liveData.postValue(ResponseOnceFilm(ResponseFilm.ERROR_NETWORK, null))
            getFilmWithIdDB(id, liveData)
        }
    }

    private suspend fun getFilmWithIdDB(id: Int, liveData: MutableLiveData<ResponseOnceFilm>){
        val film = dao.getFilm(id)
        liveData.postValue(ResponseOnceFilm(ResponseFilm.ERROR_NO, film))
    }

}