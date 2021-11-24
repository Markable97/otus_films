package com.glushko.films.business_logic_layer.interactor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.glushko.films.data_layer.datasource.NetworkService
import com.glushko.films.data_layer.datasource.response.ResponseFilm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse

class UseCaseRepository {

    suspend fun getFilm(page: Int, liveData: MutableLiveData<ResponseFilm>) {
        val response = NetworkService.makeNetworkService().getFilm(page).awaitResponse()
        if(response.isSuccessful){
            liveData.postValue(response.body()?.apply {
                isSuccess = true
            })
        }else{
            liveData.postValue(ResponseFilm(pagesCount = 0, isSuccess = false))
        }
    }
}