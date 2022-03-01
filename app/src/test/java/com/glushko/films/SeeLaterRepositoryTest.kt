package com.glushko.films

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.glushko.films.business_logic_layer.domain.SeeLaterFilm
import com.glushko.films.business_logic_layer.interactor.SeeLaterRepository
import com.glushko.films.data_layer.repository.FilmsDao
import com.glushko.films.presentation_layer.vm.ViewModelSeeLater
import io.reactivex.Single
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SeeLaterRepositoryTest {
    @get:Rule
    val schedulers = RxImmediateSchedulerRule()
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private lateinit var viewModel: ViewModelSeeLater


    @Test
    fun getFilmsSeeLaterFromDB(){
        val dao = Mockito.mock(FilmsDao::class.java)
        Mockito.`when`(dao.getSeeLaterFilms()).thenReturn(Single.just(seeLaterSample()))
        val repository = SeeLaterRepository(dao)
        viewModel = ViewModelSeeLater(repository)
        viewModel.getSeeLaterFilms()
        Assert.assertNotNull(viewModel.liveDataSeeLater)
        println("Тестовые записи ${viewModel.liveDataSeeLater.value}")
        println("${viewModel.liveDataSeeLater.hasActiveObservers()}")
        Assert.assertTrue(viewModel.liveDataSeeLater.value?.isNotEmpty()?:false)
    }

    private fun seeLaterSample(): List<SeeLaterFilm>{
        return listOf(
            SeeLaterFilm(1, "Test 1", 2022, 1, 1, 12, 12),
            SeeLaterFilm(2, "Test 2", 2022, 1, 2, 13, 13)
        )
    }

    @Test
    fun addSeeLaterFilmSavesFilmInDatabase(){
        val film = seeLaterSample().first()
        val dao = Mockito.mock(FilmsDao::class.java)
        val repository = SeeLaterRepository(dao)
        viewModel = ViewModelSeeLater(repository)
        viewModel.addSeeLaterFilm(film)

        Assert.assertTrue(viewModel.liveDataAddSeeLaterFilm.value ?:false)
    }

    @Test(expected = Exception::class)
    fun addSeeLaterFilmSavesFilmInDatabaseException (){
        val film = seeLaterSample().first()
        val dao = Mockito.mock(FilmsDao::class.java)
        Mockito.`when`(dao.addSeeLaterFilm(film)).thenThrow(Exception())
        val repository = SeeLaterRepository(dao)
        viewModel = ViewModelSeeLater(repository)
        viewModel.addSeeLaterFilm(film)
        Assert.assertFalse(viewModel.liveDataAddSeeLaterFilm.value ?:false)
    }
}