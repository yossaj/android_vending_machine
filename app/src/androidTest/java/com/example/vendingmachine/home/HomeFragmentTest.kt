package com.example.vendingmachine.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.*
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.vendingmachine.R
import com.example.vendingmachine.data.TaskDatabase
import com.example.vendingmachine.tasks.TasksFragment
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.mock

@RunWith(AndroidJUnit4ClassRunner::class)
class HomeFragmentTest{

 @Test
 fun testButtons(){
     val navController = mockk<NavController>(relaxed = true)
     navController.setGraph(R.navigation.navigation)
     val scenario = launchFragmentInContainer(Bundle(), R.style.VendingTheme){
         HomeFragment()
     }
     scenario.onFragment { Navigation.setViewNavController(it.requireView(), navController) }

 }


}