package com.dicoding.mygithub2.ui.main

import android.view.KeyEvent
import androidx.test.core.app.ActivityScenario
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.dicoding.mygithub2.R
import com.dicoding.mygithub2.ui.splash.SplashActivity

@RunWith(AndroidJUnit4ClassRunner::class)
class AppTest {

    private val dummySearch1 = "jake"
    private val dummySearch2 = "sidiqpermana"
    private val dummySearch3 = "hizkifirst"
    private val dummySearchNotFound = "hizkifirsthartoko"

    @Before
    fun setup() {
        ActivityScenario.launch(SplashActivity::class.java)
    }

    @Test
    fun assertTestSearchUser() {
        onView(withId(R.id.search)).perform(click())
        onView(withId(R.id.search_src_text)).perform(typeText(dummySearch1), closeSoftKeyboard())
        onView(withId(R.id.search_src_text)).perform(pressKey(KeyEvent.KEYCODE_ENTER))

        Thread.sleep(DURATION_1S * 2)

        onView(withId(R.id.search_close_btn)).perform(click())
        onView(withId(R.id.search_src_text)).perform(typeText(dummySearch2), closeSoftKeyboard())
        onView(withId(R.id.search_src_text)).perform(pressKey(KeyEvent.KEYCODE_ENTER))

        Thread.sleep(DURATION_1S * 2)

        onView(withId(R.id.search_close_btn)).perform(click())
        onView(withId(R.id.search_src_text)).perform(
            typeText(dummySearchNotFound),
            closeSoftKeyboard()
        )
        onView(withId(R.id.search_src_text)).perform(pressKey(KeyEvent.KEYCODE_ENTER))

        Thread.sleep(DURATION_1S * 2)

        onView(withId(R.id.search_close_btn)).perform(click())
        onView(withId(R.id.search_src_text)).perform(typeText(dummySearch3), closeSoftKeyboard())
        onView(withId(R.id.search_src_text)).perform(pressKey(KeyEvent.KEYCODE_ENTER))

        Thread.sleep(DURATION_1S)

        onView(withId(R.id.rv_users)).perform(
            RecyclerViewActions.actionOnItemAtPosition<UserAdapter.ViewHolder>(
                0,
                click()
            )
        )

        Thread.sleep(DURATION_1S * 2)

        onView(withId(R.id.fab_add_favorite)).perform(click())
        Thread.sleep(DURATION_1S)

        onView(isRoot()).perform(pressBack())

        onView(withId(R.id.preferences)).perform(click())
        Thread.sleep(DURATION_1S)
        onView(withId(R.id.switch_theme)).perform(click())
        Thread.sleep(DURATION_1S)

        onView(isRoot()).perform(pressBack())
        Thread.sleep(DURATION_1S)

        onView(withId(R.id.favorite_users)).perform(click())
        Thread.sleep(DURATION_1S / 2)
        onView(withId(R.id.rv_favorites)).perform(
            RecyclerViewActions.actionOnItemAtPosition<UserAdapter.ViewHolder>(
                0,
                click()
            )
        )
        Thread.sleep(DURATION_1S * 2)

        onView(withId(R.id.fab_add_favorite)).perform(click())
        Thread.sleep(DURATION_1S)
        onView(isRoot()).perform(pressBack())
        Thread.sleep(DURATION_1S)
    }

    companion object {
        private const val DURATION_1S = 1000L
    }
}