package com.glushko.films

import com.glushko.films.domain.models.SeeLaterFilm
import org.junit.Test

import org.junit.Assert.*
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    private val calendar: Calendar = Calendar.getInstance()
    private var year = calendar.get(Calendar.YEAR)
    private var month = calendar.get(Calendar.MONTH)
    private var day = calendar.get(Calendar.DAY_OF_MONTH)
    private var hour = calendar.get(Calendar.HOUR_OF_DAY)
    private var minute = calendar.get(Calendar.MINUTE)
    var seeLaterFilm: SeeLaterFilm = SeeLaterFilm(100, "Test Film", year, month, day, hour, minute)

    var seeLaterFilmUpdatable:SeeLaterFilm = (SeeLaterFilm(100, "Test Film", year+1, month+1, day+1, hour+1, minute+1))

    @Test
    fun checkingTimeUpdateTrue() {
        seeLaterFilm.updateTime(year+1, month+1, day+1, hour+1, minute+1)
        assertEquals(seeLaterFilmUpdatable, seeLaterFilm)

    }

    @Test
    fun checkingTimeUpdateFalse() {
        seeLaterFilm.updateTime(year+2, month+1, day+1, hour+1, minute+1)
        assertNotEquals(seeLaterFilmUpdatable, seeLaterFilm)

    }


}