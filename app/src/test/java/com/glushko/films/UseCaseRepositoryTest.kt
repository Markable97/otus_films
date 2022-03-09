package com.glushko.films

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.glushko.films.domain.models.AboutFilm
import com.glushko.films.domain.interactor.UseCaseRepository
import com.glushko.films.data.datasource.ApiService
import com.glushko.films.data.datasource.response.ResponseFilm
import com.glushko.films.data.repository.FilmsDao
import io.reactivex.Single
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito

class UseCaseRepositoryTest {
    @get:Rule
    val schedulers = RxImmediateSchedulerRule()
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var useCase: UseCaseRepository
    private lateinit var liveData: MutableLiveData<ResponseFilm>

    private fun sample(): List<AboutFilm>{
        return listOf(
            AboutFilm(id = 1, name = "Test 1", img = ".img", typeList = "list"),
            AboutFilm(id = 1, name = "Test 2", img = ".img", typeList = "list"),
            AboutFilm(id = 1, name = "Test 3", img = ".img", typeList = "list")
        )
    }

    private fun emptySample() = emptyList<AboutFilm>()

    @Test
    fun searchFilmFromDBisTrue(){
        val searchText = "Test"
        liveData = MutableLiveData()
        val dao = Mockito.mock(FilmsDao::class.java)
        val api = Mockito.mock(ApiService::class.java)
        Mockito.`when`(dao.searchFilm(searchText)).thenReturn(Single.just(sample()))
        useCase = UseCaseRepository(api, dao)
        useCase.searchFilm(searchText, liveData)
        Assert.assertEquals(true, liveData.value?.films?.isNotEmpty())
    }

    @Test
    fun searchFilmFromDBisFalse(){
        val searchText = "Test"
        liveData = MutableLiveData()
        val dao = Mockito.mock(FilmsDao::class.java)
        val api = Mockito.mock(ApiService::class.java)
        Mockito.`when`(dao.searchFilm(searchText)).thenReturn(Single.just(emptySample()))
        useCase = UseCaseRepository(api, dao)
        useCase.searchFilm(searchText, liveData)
        Assert.assertEquals(false, liveData.value?.films?.isNotEmpty())
    }

}