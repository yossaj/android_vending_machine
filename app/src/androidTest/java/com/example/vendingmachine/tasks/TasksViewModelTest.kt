package com.example.vendingmachine.tasks

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.vendingmachine.data.Task
import com.example.vendingmachine.data.TaskDatabase
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

class TasksViewModelTest {

    private lateinit var database: TaskDatabase
    private lateinit var tasksViewModel : TasksViewModel
    private lateinit var tasks: List<Task>

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun init() = runBlockingTest{
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, TaskDatabase::class.java).build()
        tasks = fakeData()
        database.taskDao.insertTask(tasks[0])
        database.taskDao.insertTask(tasks[1])
        database.taskDao.insertTask(tasks[2])
        tasksViewModel = TasksViewModel(database)

    }

    @After
    fun closeDb(){
        database.close()
    }



    @Test
    fun getAllTasks() {
        tasksViewModel.allTasks


    }

    @Test
    fun get_currentTask() {
    }

    @Test
    fun getCurrentTask() {
    }

    @Test
    fun incrementCoinSwitch() {
    }

    @Test
    fun updateTaskWhenComplete() {
    }

    @Test
    fun updateTask() {
    }

    @Test
    fun handleCheckUnCheck() {
    }

    @Test
    fun deleteAllTasks() {
    }

    @Test
    fun deleteAll() {
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

    @Test
    fun resetUponNavigationToViewTask() {
    }

    @Test
    fun getDatasource() {
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