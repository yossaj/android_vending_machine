package com.example.vendingmachine.home


import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.vendingmachine.getOrAwaitValue
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations


class HomeViewModelTest {

    @Mock
    lateinit var mockApplicationContext: Context

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    lateinit var homeViewModel: HomeViewModel

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this);
        homeViewModel = HomeViewModel()
    }

    @Test
    fun addToBalance() {
        homeViewModel.addToBalance()
        homeViewModel.addToBalance()
        homeViewModel.addToBalance()
        val balance = homeViewModel.balance
        assertThat(balance, `is`(300))
    }

    @Test
    fun addToBalanceAndConvertToString() {
        homeViewModel.addToBalanceAndConvertToString()
        homeViewModel.addToBalanceAndConvertToString()
        val balance = homeViewModel.balanceString.getOrAwaitValue()
        assertThat(balance, `is`("200"))
    }

}