package com.example.vendingmachine.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vendingmachine.data.models.Task
import com.example.vendingmachine.data.persistence.HabitDao
import com.example.vendingmachine.data.persistence.TaskDao
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*

class UserRepository constructor(private val taskDao: TaskDao, private val habitDao: HabitDao){

    val TAG = "FIRE"
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    val remoteDb = Firebase.firestore

    var allTasks = taskDao.getTasks()

    fun addTaskFireStore() {

    }

    fun syncWithFireStore(){
        var localList = listOf<Task>()
        allTasks.value?.let {
            localList = it
        }
        remoteDb.collection("tasks").addSnapshotListener{
                snapshot, exception ->
            val fsTasks = snapshot?.toObjects(Task::class.java)
            fsTasks?.let {
                if(!it.equals(localList)){
                    Log.d("FIRE", "Lists don't match")
                    val combinedTaskList = it.plus(localList)
                    val newTasks = combinedTaskList
                        .groupBy { it.id }
                        .filter { it.value.size == 1 }
                        .flatMap { it.value }
                    newTasks.forEach{
                        Log.d("FIRE", "New Items are ${it.title}")
                        if(localList.contains(it)){
//                            add to firestore
                        }else{
                            addTaskFromFireStore(it)
                        }
                    }

                }else{
                    Log.d("FIRE", "Lists do match")
                }
            }
        }
    }

    val _currentTask = MutableLiveData<Task>()
    val currentNewTask : LiveData<Task>
        get() = _currentTask

    fun updateOnComplete(task: Task){
        uiScope.launch {
            withContext(Dispatchers.IO) {
                taskDao.updateTask(task)
            }
        }
    }

    fun deleteTask(){
        uiScope.launch {
            _currentTask.value?.id?.let {
                withContext(Dispatchers.IO) {
                    taskDao.deleteTaskById(it)
                }
            }
        }
    }

    fun addTask(){
        uiScope.launch {
            currentNewTask.value?.let {
                withContext(Dispatchers.IO){
                    taskDao.insertTask(it)
                }
            }
        }
    }

    fun addTaskFromFireStore(task: Task){
        uiScope.launch {
            withContext(Dispatchers.IO){
                taskDao.insertTask(task)
            }
        }
    }

    fun deleteAllTasks(){
        uiScope.launch {
            withContext(Dispatchers.IO) {
                taskDao.deleteAllTasks()
            }
        }
    }

    fun resetCurrentTask(){
        _currentTask.value = null
    }

    fun setCurrentTask(task: Task){
        _currentTask.value = task
    }

    var allHabits = habitDao.getHabits()


}