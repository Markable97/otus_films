package com.glushko.films.presentation_layer.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glushko.films.business_logic_layer.domain.SeeLaterFilm
import com.glushko.films.business_logic_layer.interactor.SeeLaterRepository
import com.glushko.films.data_layer.utils.TAG
import kotlinx.coroutines.launch
import javax.inject.Inject

class ViewModelSeeLater constructor(private val useCase: SeeLaterRepository) : ViewModel() {


    private val _liveDataSeeLater = MutableLiveData<List<SeeLaterFilm>>()
    val liveDataSeeLater: LiveData<List<SeeLaterFilm>> = _liveDataSeeLater

    fun getSeeLaterFilms() {
        viewModelScope.launch {
            val films = useCase.getSeeLaterFilms()
            _liveDataSeeLater.postValue(films)
        }
    }

    fun addSeeLaterFilm(film: SeeLaterFilm){
        viewModelScope.launch {
            useCase.addSeeLaterFilm(film)
            Log.d(TAG, "Добавление фильма в список посмотреть позже")
        }
    }


}