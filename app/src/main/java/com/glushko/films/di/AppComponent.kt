package com.glushko.films.di

import com.glushko.films.App
import com.glushko.films.di.module.AppModule
import com.glushko.films.di.module.NetworkModule
import com.glushko.films.di.module.RepositoryModule
import com.glushko.films.presentation.ui.MainActivity
import com.glushko.films.presentation.ui.detail_film.FragmentDetailFilm
import com.glushko.films.presentation.ui.favorite.FragmentFavorites
import com.glushko.films.presentation.ui.films.FragmentFilms
import com.glushko.films.presentation.ui.see_later.FragmentSeeLater
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, RepositoryModule::class])
interface AppComponent {
    fun inject(context: App)
    fun inject(activity: MainActivity)
    fun inject(fragment: FragmentFilms)
    fun inject(fragment: FragmentFavorites)
    fun inject(fragment: FragmentSeeLater)
    fun inject(fragment: FragmentDetailFilm)
}