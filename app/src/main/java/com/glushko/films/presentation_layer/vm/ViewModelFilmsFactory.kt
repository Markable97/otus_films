package com.glushko.films.presentation_layer.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
/*В констурктор можно допустим передать какие то парметры, чтобы они перешли во ViewModel*/
class ViewModelFilmsFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ViewModelFilms() as T
    }
}