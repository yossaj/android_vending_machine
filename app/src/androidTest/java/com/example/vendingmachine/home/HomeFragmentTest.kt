package com.example.vendingmachine.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.vendingmachine.R
import com.example.vendingmachine.data.TaskDatabase
import com.example.vendingmachine.tasks.TasksFragment
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.mock

@RunWith(AndroidJUnit4::class)
class HomeFragmentTest{

 @Test
 fun testButtons(){
     val navController = mock(NavController::class.java)
     launchFragmentInContainer<HomeFragment>(Bundle(), R.style.VendingTheme){
         HomeFragment().also { fragment -> fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
             if (viewLifecycleOwner != null) {
                 // The fragment’s view has just been created
                 Navigation.setViewNavController(fragment.requireView(), navController)
             }
         } }
     }
 }


}