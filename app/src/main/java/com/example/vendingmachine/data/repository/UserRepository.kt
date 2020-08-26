package com.example.vendingmachine.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vendingmachine.data.models.Task
import com.example.vendingmachine.utils.Constants.COMPLETED
import com.example.vendingmachine.utils.Constants.TASKS
import com.example.vendingmachine.utils.Constants.UPDATE_TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*

class UserRepository constructor(private val remoteDb: FirebaseFirestore, private val firebaseAuth: FirebaseAuth) {

    private val currentUser = firebaseAuth.currentUser

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    var _allTasks = MutableLiveData<List<Task>>()
    val allTasks: LiveData<List<Task>>
        get() = _allTasks

    val _period = MutableLiveData<Int>(1)
    val period: LiveData<Int>
        get() = _period


    fun listenToFireStoreChanges() {
        remoteDb.collection("users")
            .document(getUid())
            .collection("tasks")
            .whereEqualTo("period", period.value)
            .addSnapshotListener { snapshot, exception ->
                snapshot?.let {
                    val remoteTasks: List<Task> = it.toObjects(Task::class.java) as List<Task>
                    _allTasks.postValue(remoteTasks)
                }
            }

    }

    fun incrementPeriod() {
        _period.value?.let { periodTemp ->
            if (periodTemp <= 2) {
                _period.postValue(periodTemp + 1)
            }
        }

    }


    fun getUid() : String{
        currentUser?.let {
            val uid = it.uid
            return  uid
        }
        return "1234"
    }

    fun decrementPeriod() {
        _period.value?.let { periodTemp ->
            if (periodTemp >= 0) {
                _period.postValue(periodTemp - 1)
            }
        }
    }


    val _currentTask = MutableLiveData<Task>()
    val currentNewTask: LiveData<Task>
        get() = _currentTask

    fun updateOnComplete(task: Task) {
        uiScope.launch {
            val docRef = remoteDb.collection("users").document(getUid()).collection(TASKS).document(task.id)

            docRef
                .update(COMPLETED, task.isCompleted)
                .addOnSuccessListener { Log.d(UPDATE_TAG, "Task successfully updated!") }
                .addOnFailureListener { e -> Log.w(UPDATE_TAG, "Error updating document", e) }
        }
    }

    fun deleteTask() {
        uiScope.launch {
            _currentTask.value?.id?.let { taskId ->
                remoteDb.collection("users").document(getUid()).collection(TASKS).document(taskId).delete()
                    .addOnCompleteListener {

                    }
                    .addOnFailureListener {

                    }
            }
        }
    }

    fun addTask() {
        uiScope.launch {
            withContext(Dispatchers.Main) {
                currentNewTask.value?.let { task ->
                    addTaskToFireStore(task)
                }
            }

        }
    }

    fun addTaskToFireStore(task: Task) {
        remoteDb.collection("users").document(getUid()).collection(TASKS).document(task.id).set(task)
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