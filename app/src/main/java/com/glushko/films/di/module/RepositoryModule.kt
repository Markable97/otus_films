package com.glushko.films.di.module

import androidx.lifecycle.ViewModelProvider
import com.glushko.films.business_logic_layer.interactor.SeeLaterRepository
import com.glushko.films.business_logic_layer.interactor.UseCaseRepository
import com.glushko.films.data_layer.datasource.ApiService
import com.glushko.films.presentation_layer.vm.ViewModelFilmsFactory
import com.glushko.films.presentation_layer.vm.ViewModelSeeLaterFactory
import com.google.android.gms.common.annotation.NonNullApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class RepositoryModule {

    @Provides
    fun provideViewModelFilms(useCaseRepository: UseCaseRepository): ViewModelFilmsFactory{
        return ViewModelFilmsFactory(useCaseRepository)
    }

    companion object{
        @Provides
        fun provideViewModelSeeLater(seeLaterRepository: SeeLaterRepository): ViewModelSeeLaterFactory{
            return ViewModelSeeLaterFactory(seeLaterRepository)
        }
    }
}