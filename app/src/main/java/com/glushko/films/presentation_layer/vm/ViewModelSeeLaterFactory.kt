package com.glushko.films.presentation_layer.vm

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import com.glushko.films.business_logic_layer.interactor.SeeLaterRepository
import com.glushko.films.business_logic_layer.interactor.UseCaseRepository
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Provider

/*В констурктор можно допустим передать какие то парметры, чтобы они перешли во ViewModel*/
class ViewModelSeeLaterFactory @Inject constructor(private val useCaseSeeLater: SeeLaterRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ViewModelSeeLater(useCaseSeeLater) as T
    }

}