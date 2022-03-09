package com.glushko.films

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.*
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.glushko.films.presentation.ui.MainActivity
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    private var mIdlingResource: IdlingResource? = null
    @Before
    fun registerIdlingResource() {
        activityScenarioRule.scenario.onActivity { activity ->
            mIdlingResource = activity.idlingResource
            // To prove that the test fails, omit this call:
            IdlingRegistry.getInstance().register(mIdlingResource)
        }
    }

    @Test
    fun addToFavorite_hasCheckFavorite () {

        Espresso.onView(ViewMatchers.withId(R.id.recyclerFilm))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, clickOnViewChild(R.id.btnLike)))
        val nameFilm = "Этерна: Часть первая"//getText(onView(withId(R.id.tvFilm)), 1)
        Espresso.onView(ViewMatchers.withId(R.id.menu_favorite_films)).perform(click())
        //Espresso.onView(ViewMatchers.withId(R.id.recyclerFavorite))
        Espresso.onView(Matchers.allOf(withId(R.id.tvFilmNameFavorite), withText(nameFilm)))
            .check(matches(isDisplayed()))
    }

    fun getText(matcher: Matcher<View?>?): String? {
        val stringHolder = arrayOf<String?>(null)
        onView(matcher).perform(object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(TextView::class.java)
            }

            override fun getDescription(): String {
                return "getting text from a TextView"
            }

            override fun perform(uiController: UiController?, view: View) {
                val tv = view as TextView //Save, because of check in getConstraints()
                stringHolder[0] = tv.text.toString()
            }
        })
        return stringHolder[0]
    }


    @After
    fun unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource)
        }
    }

    private fun clickOnViewChild(viewId: Int) = object : ViewAction {
        override fun getConstraints() = null

        override fun getDescription() = "Click on a child view with specified id."

        override fun perform(uiController: UiController, view: View) = click().perform(uiController, view.findViewById<View>(viewId))
    }

}