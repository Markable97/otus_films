package com.glushko.films.di.module

import com.glushko.films.domain.interactor.SeeLaterRepository
import com.glushko.films.domain.interactor.UseCaseRepository
import com.glushko.films.presentation.vm.ViewModelFilmsFactory
import com.glushko.films.presentation.vm.ViewModelSeeLaterFactory
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    fun provideViewModelFilms(useCaseRepository: UseCaseRepository): ViewModelFilmsFactory {
        return ViewModelFilmsFactory(useCaseRepository)
    }

    companion object {
        @Provides
        fun provideViewModelSeeLater(seeLaterRepository: SeeLaterRepository): ViewModelSeeLaterFactory {
            return ViewModelSeeLaterFactory(seeLaterRepository)
        }
    }
}