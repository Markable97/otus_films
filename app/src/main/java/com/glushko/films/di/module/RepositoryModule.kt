package com.glushko.films.di.module

import com.glushko.films.domain.interactor.SeeLaterRepository
import com.glushko.films.domain.interactor.FilmsRepository
import com.glushko.films.data.repository.FilmsRepositoryImpl
import com.glushko.films.data.repository.SeeLaterRepositoryImpl
import com.glushko.films.presentation.vm.ViewModelFilmsFactory
import com.glushko.films.presentation.vm.ViewModelSeeLaterFactory
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    fun provideFilmsRepo(impl: FilmsRepositoryImpl): FilmsRepository = impl

    @Provides
    fun provideViewModelFilms(filmsRepository: FilmsRepositoryImpl): ViewModelFilmsFactory {
        return ViewModelFilmsFactory(filmsRepository)
    }


    companion object {

        @Provides
        fun provideSeeLaterRepo(impl: SeeLaterRepositoryImpl):SeeLaterRepository = impl

        @Provides
        fun provideViewModelSeeLater(seeLaterRepository: SeeLaterRepositoryImpl): ViewModelSeeLaterFactory {
            return ViewModelSeeLaterFactory(seeLaterRepository)
        }
    }
}