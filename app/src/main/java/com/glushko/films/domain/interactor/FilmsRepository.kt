package com.glushko.films.domain.interactor

import androidx.lifecycle.MutableLiveData
import com.glushko.films.domain.models.AboutFilm
import com.glushko.films.domain.models.FavoriteFilm
import com.glushko.films.data.datasource.response.ResponseFilm
import com.glushko.films.data.datasource.response.ResponseOnceFilm
import io.reactivex.disposables.Disposable


interface FilmsRepository {

    fun getFilm(page: Int, liveData: MutableLiveData<ResponseFilm>): Disposable

    fun addFavoriteFilm(film: FavoriteFilm): Disposable

    fun deleteFavoriteFilm(film: FavoriteFilm): Disposable

    fun getFavoriteFilms(liveData: MutableLiveData<List<FavoriteFilm>>): Disposable

    fun addComment(film: AboutFilm): Disposable

    fun getFilmWithId(id: Int, liveData: MutableLiveData<ResponseOnceFilm>): Disposable

    fun searchFilm(text: String, liveDataFilm: MutableLiveData<ResponseFilm>): Disposable

}