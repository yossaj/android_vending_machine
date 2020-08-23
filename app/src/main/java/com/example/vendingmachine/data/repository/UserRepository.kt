package com.example.vendingmachine.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vendingmachine.data.models.Task
import com.example.vendingmachine.utils.Constants.COMPLETED
import com.example.vendingmachine.utils.Constants.TASKS
import com.example.vendingmachine.utils.Constants.UPDATE_TAG
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*

class UserRepository constructor(private val remoteDb: FirebaseFirestore) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    val _allTasks = MutableLiveData<List<Task>>()
    val allTasks: LiveData<List<Task>>
        get() = _allTasks

    fun listenToFireStoreChanges() {
        remoteDb.collection(TASKS).addSnapshotListener { snapshot, exception ->
            val remoteTasks: List<Task> = snapshot?.toObjects(Task::class.java) as List<Task>
            _allTasks.postValue(remoteTasks)
        }
    }

    val _currentTask = MutableLiveData<Task>()
    val currentNewTask: LiveData<Task>
        get() = _currentTask

    fun updateOnComplete(task: Task) {
        uiScope.launch {
            val docRef = remoteDb.collection(TASKS).document(task.id)

            docRef
                .update(COMPLETED, task.isCompleted)
                .addOnSuccessListener { Log.d(UPDATE_TAG, "Task successfully updated!") }
                .addOnFailureListener { e -> Log.w(UPDATE_TAG, "Error updating document", e) }
        }
    }

    fun deleteTask() {
        uiScope.launch {
            _currentTask.value?.id?.let { taskId ->
                remoteDb.collection("tasks").document(taskId).delete()
                    .addOnCompleteListener {

                    }
                    .addOnFailureListener {

                    }
            }
        }
    }

    fun addTask() {
        uiScope.launch {
            withContext(Dispatchers.Main){
                currentNewTask.value?.let { task ->
                    addTaskToFireStore(task)
                }
            }

        }
    }

    fun addTaskToFireStore(task: Task) {
        remoteDb.collection("tasks").document(task.id).set(task)
    }

    fun deleteAllTasks() {
        uiScope.launch {

        }
    }

    fun resetCurrentTask() {
        _currentTask.value = null
    }

    fun setCurrentTask(task: Task) {
        _currentTask.value = task
    }


    init {
        listenToFireStoreChanges()
    }

}