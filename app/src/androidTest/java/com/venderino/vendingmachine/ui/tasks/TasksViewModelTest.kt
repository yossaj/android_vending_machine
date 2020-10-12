package com.venderino.vendingmachine.ui.tasks

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.venderino.vendingmachine.data.models.Task
import com.venderino.vendingmachine.getOrAwaitValueAndroid
import org.hamcrest.CoreMatchers.`is`
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

class TasksViewModelTest {

    private lateinit var tasksViewModel : TasksViewModel
    private lateinit var tasks: List<Task>

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun init(){
        val context = ApplicationProvider.getApplicationContext<Context>()
    }

//    @After
//    fun closeDb(){
//        database.close()
//    }


    @Test
    fun getAllTasks() {
        val requestedtasks = tasksViewModel.allTasks.getOrAwaitValueAndroid()
        assertThat(requestedtasks?.size, `is`(3))

    }


    @Test
    fun triggerAddTaskNav() {
    }

    @Test
    fun triggerViewTaskNav() {
    }

    @Test
    fun resetViewTaskTrigger() {
    }

    @Test
    fun navigateToViewTaskAndPassTask() {
    }

    @Test
    fun resetCurrentTask() {


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