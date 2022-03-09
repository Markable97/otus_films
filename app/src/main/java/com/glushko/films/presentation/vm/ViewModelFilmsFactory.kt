package com.glushko.films.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.glushko.films.domain.interactor.UseCaseRepository
import javax.inject.Inject

/*В констурктор можно допустим передать какие то парметры, чтобы они перешли во ViewModel*/
class ViewModelFilmsFactory @Inject constructor(private val useCaseRepository: UseCaseRepository) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ViewModelFilms(useCaseRepository) as T
    }

}