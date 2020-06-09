package com.example.vendingmachine

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.vendingmachine.tasks.TasksAdapter
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityTest{

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity>
            = ActivityTestRule(MainActivity::class.java)

    @Test
    fun snackBarIsDisplauyedWhenFundsZero(){
        onView(withId(R.id.cat_button))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(R.string.no_funds)))
    }

    @Test
    fun navigateToTaskFragment(){
        onView(withId(R.id.new_task)).perform(click())
        onView(withId(R.id.task_habit_list))
            .perform(RecyclerViewActions.actionOnItemAtPosition<TasksAdapter.TaskViewHolder>(0, click()))
        pressBack()
    }

    @Test
    fun dragCoinAndAddToBalance(){
//        onView(withId(R.id.coins)).check(matches(isDisplayed()))
        onView(withId(R.id.balance_text)).check(matches(isDisplayed())).check(matches(withText("000")))
        onView(withId(R.id.new_task)).perform(click())
//        onView(withId(R.id.dog_button)).perform()
    }

    fun dragDown(x: Int, y: Int): ViewAction {

        val dragLocation =
            CoordinatesProvider { view ->
            val screenPos = IntArray(2)
            view.getLocationOnScreen(screenPos)
            val screenX = screenPos[0] + x.toFloat()
            val screenY = screenPos[1] + y.toFloat()
            floatArrayOf(screenX, screenY)
        }

        return GeneralSwipeAction(
            Swipe.SLOW, GeneralLocation.CENTER,
            dragLocation, Press.FINGER
        )
    }

}