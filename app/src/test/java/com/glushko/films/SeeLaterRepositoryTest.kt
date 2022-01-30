package com.glushko.films

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.glushko.films.business_logic_layer.domain.SeeLaterFilm
import com.glushko.films.business_logic_layer.interactor.SeeLaterRepository
import com.glushko.films.data_layer.repository.FilmsDao
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.observers.ConsumerSingleObserver
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.*
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SeeLaterRepositoryTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    @get:Rule
    val schedulers = RxImmediateSchedulerRule()
    @Mock
    private lateinit var dao: FilmsDao
    private lateinit var liveData: MutableLiveData<List<SeeLaterFilm>>
    private lateinit var repository: SeeLaterRepository
    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repository = SeeLaterRepository(dao)
        liveData = MutableLiveData()
    }

    @Test
    fun checkingGetSeeLaterFilmsIsNotEmpty(){
        `when`(dao.getSeeLaterFilms()).thenReturn(
            Single.just(seeLaterSample())
        )

        repository.getSeeLaterFilms(liveData)
        Assert.assertEquals(true, liveData.value?.isNotEmpty())
    }

    private fun seeLaterSample(): List<SeeLaterFilm>{
        return listOf(
            SeeLaterFilm(1, "Test 1", 2022, 1, 1, 12, 12),
            SeeLaterFilm(2, "Test 2", 2022, 1, 2, 13, 13)
        )
    }

    @Test
    fun checkingGetSeeLaterFilmsIsEmpty(){
        `when`(dao.getSeeLaterFilms()).thenReturn(
            Single.just(emptyList())
        )

        repository.getSeeLaterFilms(liveData)
        Assert.assertEquals(true, liveData.value?.isEmpty())
    }

    @After
    fun end(){
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }

}