package com.glushko.films.presentation_layer.vm

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import com.glushko.films.business_logic_layer.interactor.UseCaseRepository
import javax.inject.Inject
import javax.inject.Provider

/*В констурктор можно допустим передать какие то парметры, чтобы они перешли во ViewModel*/
class ViewModelFilmsFactory @Inject constructor(private val useCaseRepository: UseCaseRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ViewModelFilms(useCaseRepository) as T
    }

}