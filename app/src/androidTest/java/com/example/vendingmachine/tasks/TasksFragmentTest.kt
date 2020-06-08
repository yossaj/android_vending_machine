package com.example.vendingmachine.tasks

import android.content.Context
import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.vendingmachine.R
import com.example.vendingmachine.data.Task
import com.example.vendingmachine.data.TaskDatabase
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class TasksFragmentTest {
//    private lateinit var database: TaskDatabase
//    private lateinit var tasksViewModel : TasksViewModel
//    private lateinit var tasks: List<Task>
//
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun init(){
        MockitoAnnotations.initMocks(this)
        val factory = FragmentFactory()
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext())
        navController.setGraph(R.navigation.navigation)
        val scenario = launchFragmentInContainer<TasksFragment>(Bundle(), R.style.VendingTheme, factory)
        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
        }


    }

    @After
    fun closeDb() = runBlockingTest{
//        database.close()
    }

    @Test
    fun getSharedPreferences() {
    }

    @Test
    fun setSharedPreferences() {
    }

    @Test
    fun getCoinCount() {
    }

    @Test
    fun setCoinCount() {
    }

    @Test
    fun testGetCoinCount() {
    }

    @Test
    fun incrementCoinCount() {
    }

    fun fakeData() : List<Task>{
        val taskList = mutableListOf<Task>()
        val task1 = Task("Task1", "Tasky")
        val task2 = Task("Task2", "Tasky")
        val task3 = Task("Task3", "Tasky")
        val task4 = Task("Task4", "Tasky")
        taskList.add(task1)
        taskList.add(task2)
        taskList.add(task3)
        taskList.add(task4)
        return taskList
    }
}