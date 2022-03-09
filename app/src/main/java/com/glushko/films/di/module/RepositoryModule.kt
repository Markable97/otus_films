package com.glushko.films.di.module

import com.glushko.films.domain.interactor.SeeLaterRepository
import com.glushko.films.domain.interactor.FilmsRepository
import com.glushko.films.presentation.vm.ViewModelFilmsFactory
import com.glushko.films.presentation.vm.ViewModelSeeLaterFactory
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    fun provideViewModelFilms(filmsRepository: FilmsRepository): ViewModelFilmsFactory {
        return ViewModelFilmsFactory(filmsRepository)
    }

    companion object {
        @Provides
        fun provideViewModelSeeLater(seeLaterRepository: SeeLaterRepository): ViewModelSeeLaterFactory {
            return ViewModelSeeLaterFactory(seeLaterRepository)
        }
    }
}