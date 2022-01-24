package com.glushko.films.di

import com.glushko.films.di.module.NetworkModule
import com.glushko.films.di.module.RepositoryModule
import com.glushko.films.presentation_layer.ui.MainActivity
import com.glushko.films.presentation_layer.ui.detail_film.FragmentDetailFilm
import com.glushko.films.presentation_layer.ui.favorite.FragmentFavorites
import com.glushko.films.presentation_layer.ui.films.FragmentFilms
import com.glushko.films.presentation_layer.ui.see_later.FragmentSeeLater
import com.glushko.films.presentation_layer.vm.ViewModelFilms
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, RepositoryModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(fragment: FragmentFilms)
    fun inject(fragment: FragmentFavorites)
    fun inject(fragment: FragmentSeeLater)
    fun inject(fragment: FragmentDetailFilm)
}