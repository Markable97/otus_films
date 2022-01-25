package com.glushko.films.business_logic_layer.interactor

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.glushko.films.App
import com.glushko.films.R
import com.glushko.films.business_logic_layer.domain.AboutFilm
import com.glushko.films.business_logic_layer.domain.AboutOnceFilm
import com.glushko.films.business_logic_layer.domain.FavoriteFilm
import com.glushko.films.business_logic_layer.domain.UpdateTime
import com.glushko.films.data_layer.datasource.ApiService
import com.glushko.films.data_layer.datasource.ApiService.Companion.GET_FILMS
import com.glushko.films.data_layer.datasource.response.ResponseFilm
import com.glushko.films.data_layer.datasource.response.ResponseOnceFilm
import com.glushko.films.data_layer.repository.FilmsDao
import com.glushko.films.data_layer.utils.TYPE_FILM_LIST
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.awaitResponse
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class UseCaseRepository @Inject constructor(
    private val api: ApiService,
    private val dao: FilmsDao
) {

    companion object {
        val FRESH_TIMEOUT = TimeUnit.MINUTES.toMillis(1) //TimeUnit.DAYS.toMillis(1)
    }

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
            .map { it ->
                val currentTime = System.currentTimeMillis()
                val isRefresh = (currentTime - it.first) >= FRESH_TIMEOUT
                it.second or isRefresh
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it) {
                    getFilmsFromServer(page, liveData)
                }
            }, {
                it.printStackTrace()
                liveData.postValue(
                    ResponseFilm(
                        pagesCount = 0,
                        isSuccess = false,
                        isUpdateDB = false,
                        page = page,
                        err = ResponseFilm.ERROR_UNKNOWN
                    )
                )
            })
        return disposable
    }


    private fun getFilmsFromServer(page: Int, liveData: MutableLiveData<ResponseFilm>): Disposable {
        return api.getFilmRx(TYPE_FILM_LIST, page)
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
                when (it) {
                    is HttpException -> {
                        val error = when (it.code()) {
                            401 -> ResponseFilm.ERROR_SERVER_TOKEN
                            429 -> ResponseFilm.ERROR_SERVER_TIME_LIMIT
                            else -> ResponseFilm.ERROR_UNKNOWN
                        }
                        liveData.postValue(
                            ResponseFilm(
                                pagesCount = 0,
                                isSuccess = false,
                                isUpdateDB = false,
                                page = page,
                                err = error
                            )
                        )
                    }
                    is UnknownHostException -> {
                        liveData.postValue(
                            ResponseFilm(
                                pagesCount = 0,
                                isSuccess = false,
                                isUpdateDB = false,
                                page = page,
                                err = ResponseFilm.ERROR_NETWORK
                            )
                        )
                    }
                    else -> liveData.postValue(
                        ResponseFilm(
                            pagesCount = 0,
                            isSuccess = false,
                            isUpdateDB = false,
                            page = page,
                            err = ResponseFilm.ERROR_UNKNOWN
                        )
                    )
                }
            })
    }

    private fun mappingFilms(response: ResponseFilm): ResponseFilm {
        val films = response.films
        films.forEach {
            if (isInFavorite(it.id) == 1) {
                it.like = 1
                it.imgLike = R.drawable.ic_like
            } else {
                it.like = 0
                it.imgLike = R.drawable.ic_not_like
            }
            it.comment = getComment(it.id) ?: ""
            it.typeList = TYPE_FILM_LIST
        }
        response.films = films
        return response
    }

    private fun insertDB(response: ResponseFilm, page: Int): ResponseFilm {
        val films = response.films
        films.let {

            it.forEachIndexed { index, film ->
                film.position = it.size * (page - 1) + index + 1
            }
            dao.insertFilms(films)
        }
        response.films = films
        return response
        //println("кол-во фильтмов в бд = ${dao.getCntFilm()}")
    }

    private fun isInFavorite(id: Int): Int {
        return dao.isInFavorite(id)
    }

    fun addFavoriteFilm(film: FavoriteFilm): Disposable {
        return Single.just("")
            .subscribeOn(Schedulers.io())
            .map { dao.insertFavoriteFilm(film) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()

    }

    fun deleteFavoriteFilm(film: FavoriteFilm): Disposable {
        return dao.deleteFavoriteFilm(film)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    fun getFavoriteFilms(liveData: MutableLiveData<List<FavoriteFilm>>): Disposable {
        return dao.getFavoriteFilms()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { list ->
                liveData.postValue(list)
            }

    }

    private fun getComment(id: Int): String? {
        return dao.getCommentForFilm(id)
    }

    fun addComment(film: AboutFilm): Disposable {
        return dao.addComment(film)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    fun getFilmWithId(id: Int, liveData: MutableLiveData<ResponseOnceFilm>): Disposable {
        return api.getFilmWithID("$GET_FILMS$id")
            .subscribeOn(Schedulers.io())
            .flatMap { response ->
                Single.just(
                    getFilmWithIdDB(response.id)
                )
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ film ->
                liveData.postValue(ResponseOnceFilm(ResponseFilm.ERROR_NO, film))
            }, {
                when (it) {
                    is HttpException -> {
                        val error = when (it.code()) {
                            401 -> ResponseFilm.ERROR_SERVER_TOKEN
                            429 -> ResponseFilm.ERROR_SERVER_TIME_LIMIT
                            else -> ResponseFilm.ERROR_UNKNOWN
                        }
                        Log.d("TAG", "$error")
                        liveData.postValue(ResponseOnceFilm(error, null))
                    }
                    is UnknownHostException -> {
                        liveData.postValue(ResponseOnceFilm(ResponseFilm.ERROR_NETWORK, null))
                    }
                    else -> liveData.postValue(ResponseOnceFilm(ResponseFilm.ERROR_UNKNOWN, null))
                }
            })
    }

    private fun getFilmWithIdDB(id: Int): AboutFilm {
        return dao.getFilm(id)
    }

    fun searchFilm(text: String, liveDataFilm: MutableLiveData<ResponseFilm>): Disposable {
        return dao.searchFilm(text).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { list ->
                Log.d("TAG", "Список фильмов ${list.size} $list")
                liveDataFilm.postValue(
                    ResponseFilm(
                        list.size,
                        true,
                        isUpdateDB = true,
                        films = list,
                        page = 1,
                        err = ResponseFilm.ERROR_NO,
                        isLocalSearch = true
                    )
                )
            }
    }

}