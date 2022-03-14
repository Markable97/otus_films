package com.glushko.films

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.glushko.films.domain.models.SeeLaterFilm
import com.glushko.films.domain.interactor.SeeLaterRepository
import com.glushko.films.data.database.FilmsDao
import com.glushko.films.data.database.MainDatabase
import com.glushko.films.data.repository.SeeLaterRepositoryImpl
import org.junit.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
@Config(sdk = [26])
class RoomTest {
    @get:Rule
    var instantTaskExecutor = InstantTaskExecutorRule()
    @get:Rule
    val schedulers = RxImmediateSchedulerRule()
    private lateinit var db: MainDatabase
    private lateinit var filmDaO: FilmsDao
    private lateinit var seeLaterRepository: SeeLaterRepository
    private val seeLaterFilmSample = SeeLaterFilm(1, "Test 1", 2022, 1, 1, 12, 12)

    @Before
    @Throws(Exception::class)
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            MainDatabase::class.java
        ).allowMainThreadQueries().build()
        filmDaO = db.filmsDao()
        seeLaterRepository = SeeLaterRepositoryImpl(filmDaO)
    }

    @Test
    fun checkSeeLaterFilmAddInDB(){
        seeLaterRepository.addSeeLaterFilm(seeLaterFilmSample)
        val filmFromDb = filmDaO.getSeeLaterFilm(seeLaterFilmSample.id).blockingGet()
        Assert.assertEquals(seeLaterFilmSample, filmFromDb)
    }

    @After
    @Throws(Exception::class)
    fun closeDb() {
        db.close()
    }

}