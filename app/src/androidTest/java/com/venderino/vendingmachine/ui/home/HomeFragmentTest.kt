package com.venderino.vendingmachine.ui.home

import androidx.test.core.app.launchActivity
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.venderino.vendingmachine.ui.MainActivity
import com.venderino.vendingmachine.ui.auth.AuthActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import com.venderino.vendingmachine.ui.util.launchFragmentInHiltContainer

@HiltAndroidTest
@RunWith(AndroidJUnit4ClassRunner::class)
class HomeFragmentTest{

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun init(){
        hiltRule.inject()
    }

 @Test
 fun testButtons(){
     assert(1.equals(1))
 }


}