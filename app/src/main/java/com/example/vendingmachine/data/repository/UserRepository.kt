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
    private val uiScope = CoroutineScope(Dispatchers.IO + viewModelJob)
    val remoteDb = Firebase.firestore

    val allTasks = taskDao.getTasks()

    fun addTaskFireStore() {

    }

    fun listenToFireStoreChanges(){
        remoteDb.collection("tasks").addSnapshotListener{
                snapshot, exception ->
            snapshot
            val remoteTasks : List<Task> = snapshot?.toObjects(Task::class.java) as List<Task>
                if(!remoteTasks.equals(allTasks.value) && remoteTasks.isNotEmpty()  && !allTasks.value.isNullOrEmpty()){
                    Log.d("FIRE", "Lists don't match")
                    val combinedTaskList : List<Task> = if(remoteTasks.isEmpty())  allTasks.value!! else remoteTasks.plus(allTasks.value!!)
                    combinedTaskList.forEach{
                        Log.d("FIRE", it.toString())
                    }
                    val newTasks = combinedTaskList
                        .groupBy { it.id }
                        .filter { it.value.size == 1 }
                        .flatMap { it.value }
                    newTasks.forEach{ task ->
                        Log.d("FIRE", "New Item : ${task.title} ${task.id}")
                        if(allTasks.value!!.contains(task)){
//                            add to firestore
                            addTaskToFireStore(task)
                            Log.d("FIRE", "Add Remote ${task.toString()}")
                        }else if(remoteTasks.contains(task)){
                            Log.d("FIRE", "Added To Local : ${task.toString()}")
                            addTaskFromFireStore(task)
                        }
                    }
                }else if( remoteTasks.isNullOrEmpty() && allTasks.value.isNullOrEmpty()){
                    Log.d("FIRE", "Both Lists Empty")

                } else if(remoteTasks.isNullOrEmpty()){
                    Log.d("FIRE", "Remote List Empty")
                    allTasks.value?.let {
                        it.forEach{
                            task -> addTaskToFireStore(task)
                            Log.d("FIRE", "Add to Remote : ${task.title}")
                        }
                    }
                } else if(allTasks.value.isNullOrEmpty()){
                    Log.d("FIRE", "Local List Empty")

                    remoteTasks.forEach{
                        task -> addTaskFromFireStore(task)
                        Log.d("FIRE", "Add to Local : ${task.title}")
                    }
                }
                else{
                    Log.d("FIRE", "Lists match")
                }
            }
    }

    val _currentTask = MutableLiveData<Task>()
    val currentNewTask : LiveData<Task>
        get() = _currentTask

    fun updateOnComplete(task: Task){
        uiScope.launch {
                taskDao.updateTask(task)
        }
    }

    fun deleteTask(){
        uiScope.launch {
            _currentTask.value?.id?.let {
                    val taskid = it
                    remoteDb.collection("tasks").document(taskid).delete().addOnCompleteListener {
                        uiScope.launch { taskDao.deleteTaskById(taskid) }
                    }.addOnFailureListener {
                        uiScope.launch { taskDao.deleteTaskById(taskid) }
                    }
                }
            }
    }

    fun addTask(){
        uiScope.launch {
            currentNewTask.value?.let {
                    taskDao.insertTask(it)
            }
        }
    }

    fun addTaskFromFireStore(task: Task){
        uiScope.launch {
                taskDao.insertTask(task)
        }
    }

    fun addTaskToFireStore(task: Task){
        remoteDb.collection("tasks").document(task.id).set(task)
    }

    fun deleteAllTasks(){
        uiScope.launch {
                taskDao.deleteAllTasks()
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