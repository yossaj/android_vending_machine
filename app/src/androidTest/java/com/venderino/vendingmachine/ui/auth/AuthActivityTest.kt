package com.venderino.vendingmachine.ui.auth

import android.util.Log
import android.view.KeyEvent
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.UiDevice
import com.venderino.vendingmachine.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4ClassRunner::class)
class AuthActivityTest{

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun init(){
        hiltRule.inject()
    }

    @Test
    fun loginPath() = runBlockingTest{
        val scenario = launchActivity<AuthActivity>()
        val device = UiDevice.getInstance(getInstrumentation())
        Thread.sleep(1500)

        Log.d("CESHI", "Device pressBack Triggered");


        device.pressBack()

        onView(withId(R.id.email)).check(matches(isDisplayed()))
            .perform(click())
            .perform(typeText("yossaj@icloud.com"))

        device.pressEnter()

        onView(withId(R.id.heading)).check(matches(isDisplayed()))
        onView(withId(R.id.password)).check(matches(isDisplayed()))
        onView(withId(R.id.button_done)).check(matches(isDisplayed()))
    }

    @Test
    @ExperimentalCoroutinesApi
    fun registerPath() = runBlockingTest{
        val scenario = launchActivity<AuthActivity>()
        val device = UiDevice.getInstance(getInstrumentation())
        Thread.sleep(1500)

        Log.d("CESHI", "Device pressBack Triggered");


        device.pressBack()

        onView(withId(R.id.email)).check(matches(isDisplayed()))
            .perform(click())
            .perform(typeText("yossaj@icloud.com"))

        device.pressEnter()

        onView(withId(R.id.heading)).check(matches(isDisplayed()))
        onView(withId(R.id.password)).check(matches(isDisplayed()))
        onView(withId(R.id.button_done)).check(matches(isDisplayed()))
    }


}