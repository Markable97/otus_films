package com.glushko.films.presentation_layer.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.glushko.films.business_logic_layer.interactor.SeeLaterRepository
import javax.inject.Inject

/*В констурктор можно допустим передать какие то парметры, чтобы они перешли во ViewModel*/
class ViewModelSeeLaterFactory @Inject constructor(private val useCaseSeeLater: SeeLaterRepository) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ViewModelSeeLater(useCaseSeeLater) as T
    }

}