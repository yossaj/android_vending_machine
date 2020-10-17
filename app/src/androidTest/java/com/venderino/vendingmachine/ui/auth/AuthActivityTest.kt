package com.venderino.vendingmachine.ui.auth

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.UiDevice
import com.venderino.vendingmachine.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
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
    fun testWelcomeUi(){
        val scenario = launchActivity<AuthActivity>()
        val device = UiDevice.getInstance(getInstrumentation())
        onView(withId(R.id.email_button))
            .check(matches(isDisplayed()))
            .perform(click())

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