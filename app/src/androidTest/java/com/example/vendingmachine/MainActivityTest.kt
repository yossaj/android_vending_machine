package com.example.vendingmachine

import android.view.View
import android.view.accessibility.AccessibilityNodeInfo
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.GeneralLocation
import androidx.test.espresso.action.GeneralSwipeAction
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Swipe
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.vendingmachine.tasks.TasksAdapter
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityTest{

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity>
            = ActivityTestRule(MainActivity::class.java)

    @Before
    fun addCoinIfNonPresent(){

    }

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
            .perform(RecyclerViewActions.actionOnItemAtPosition<TasksAdapter.TaskViewHolder>
                (0, clickOnViewChild(R.id.task_item_checkbox)))
        pressBack()
        onView(withId(R.id.coins)).check(matches(isDisplayed()))
    }

    @Test
    fun dragCoinAndAddToBalance(){
        onView(withId(R.id.coins))
            .check(matches(isClickable()))
            .check(matches(isDisplayed()))
            .perform(forceClick())
        onView(withId(R.id.balance_text)).check(matches(isDisplayed())).check(matches(withText("000")))
        onView(withId(R.id.new_task)).perform(click())
//        onView(withId(R.id.dog_button)).perform()
    }

    fun dragDown(viewId: Int)= object : ViewAction {

//        val dragLocation =
//            CoordinatesProvider { view ->
//            val screenPos = IntArray(2)
//            view.getLocationOnScreen(screenPos)
//            val screenX = screenPos[0] + x.toFloat()
//            val screenY = screenPos[1] + y.toFloat()
//            floatArrayOf(screenX, screenY)
//        }

        val dragDown = GeneralSwipeAction(
        Swipe.SLOW, GeneralLocation.CENTER,
        GeneralLocation.BOTTOM_CENTER, Press.FINGER
        )

        override fun getDescription(): String = "Drag item downwards"

        override fun getConstraints() = null

        override fun perform(uiController: UiController?, view: View) = dragDown.perform(uiController, view.findViewById<View>(viewId))
    }

    fun clickOnViewChild(viewId: Int) = object : ViewAction {
        override fun getConstraints() = null

        override fun getDescription() = "Click on a child view with specified id."

        override fun perform(uiController: UiController, view: View) = click().perform(uiController, view.findViewById<View>(viewId))
    }

    fun forceClick() = object  : ViewAction {

        val dragDown = GeneralSwipeAction(
            Swipe.SLOW, GeneralLocation.CENTER,
            GeneralLocation.BOTTOM_CENTER, Press.FINGER
        )
            override fun getConstraints(): Matcher<View> {
                return allOf(isClickable(), isEnabled(), isDisplayed())
            }

            override fun getDescription(): String = "force click"


            override fun perform(
                uiController: UiController,
                view: View
            ) {
                view.performLongClick() // perform click without checking view coordinates.
                view.performAccessibilityAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_DOWN.id, null )
                uiController.loopMainThreadUntilIdle()
            }
    }
}