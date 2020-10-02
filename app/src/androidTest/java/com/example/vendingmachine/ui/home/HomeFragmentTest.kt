package com.example.vendingmachine.ui.home

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.vendingmachine.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4ClassRunner::class)
class HomeFragmentTest{

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

 @Test
 fun testButtons(){

//     val navController = mockk<NavController>(relaxed = true)
//     navController.setGraph(R.navigation.navigation)
//     val scenario = launchFragmentInContainer(Bundle(), R.style.VendingTheme){
//         HomeFragment()
//     }
//     scenario.onFragment { Navigation.setViewNavController(it.requireView(), navController) }
 }


}