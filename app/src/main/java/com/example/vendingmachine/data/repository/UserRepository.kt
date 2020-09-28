package com.example.vendingmachine.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vendingmachine.data.models.Habit
import com.example.vendingmachine.data.models.Task
import com.example.vendingmachine.utils.Constants.COMPLETED
import com.example.vendingmachine.utils.Constants.HABITS
import com.example.vendingmachine.utils.Constants.TASKS
import com.example.vendingmachine.utils.Constants.UPDATE_TAG
import com.example.vendingmachine.utils.Constants.USERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.coroutines.*
import java.util.*

class UserRepository constructor(
    private val remoteDb: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) {

    private val currentUser = firebaseAuth.currentUser

    var registeredTaskQuery = MutableLiveData<ListenerRegistration?>()
    var registeredHabitQuery = MutableLiveData<ListenerRegistration?>()

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    val _allTasks = MutableLiveData<List<Task>>()
    val allTasks: LiveData<List<Task>>
        get() = _allTasks

    var _allHabits = MutableLiveData<List<Habit>>()
    val allHabits: LiveData<List<Habit>>
        get() = _allHabits

    val _period = MutableLiveData<Int>(1)
    val period: LiveData<Int>
        get() = _period

    val _frequency = MutableLiveData<Int>(1)
    val frequency: LiveData<Int>
        get() = _frequency

    fun listenForTaskChanges() {

        uiScope.launch {
            val taskQuery = remoteDb.collection(USERS)
                .document(getUid())
                .collection("tasks")
                .orderBy("updatedAt", Query.Direction.DESCENDING)
                .whereEqualTo("period", period.value)

            registeredTaskQuery.postValue(
                taskQuery.addSnapshotListener { snapshot, exception ->
                    exception?.let {
                        Log.d("Exception", it.message)
                    }

                    snapshot?.let {
                        val remoteTasks: MutableList<Task> = mutableListOf<Task>()
                        for (document in it.documents) {
                            val task =  Task(
                                document.getString("title") as String,
                                document.getString("description") as String,
                                document.get("period").toString().toInt(),
                                document.get("colour").toString().toInt(),
                                document.get("completed") as Boolean,
                                document.get("updatedAt") as Long,
                                document.id
                            )
                            remoteTasks.add(task)
                            Log.d("Doc", "Document id added: ${document.id} - ${task.id}")
                        }

                        Log.d("Doc List", remoteTasks.size.toString())
                          _allTasks.postValue(remoteTasks)
                    }
                }
            )
        }
    }

    fun removeRegisteredTaskQuery() {
        uiScope.launch {
            if (registeredTaskQuery.value != null) {
                registeredTaskQuery.value?.let {
                    it.remove()
                }
            }
        }
    }

    fun listenForHabitChanges() {
        uiScope.launch {
            val habitQuery = remoteDb.collection(USERS)
                .document(getUid())
                .collection(HABITS)
                .whereEqualTo("frequency", frequency.value)
                .orderBy("updatedAt", Query.Direction.ASCENDING)

            registeredHabitQuery.postValue(
                habitQuery.addSnapshotListener { snapshot, exception ->
                    exception?.let {
                        Log.d("Habit Except", it.message)
                    }

                    snapshot?.let {
                        it.documents.forEach() { document -> document.id }
                        var remoteHabits: List<Habit> =
                            it.toObjects(Habit::class.java) as List<Habit>
                        _allHabits.postValue(remoteHabits)
                    }
                }
            )
        }
    }

    fun removeRegisteredHabitListener() {
        uiScope.launch {
            if (registeredHabitQuery.value != null) {
                registeredHabitQuery.value?.let {
                    it.remove()
                }
            }
        }
    }

    fun updateHabitCount(habit: Habit, count: Int) {
        val docRef = remoteDb.collection("users")
            .document(getUid())
            .collection("habits")
            .document(habit.id)

        docRef
            .update("count", count)
            .addOnSuccessListener { Log.d(UPDATE_TAG, "Habit count successfully updated!") }
            .addOnFailureListener { e -> Log.w(UPDATE_TAG, "Error updating document", e) }
    }

    fun updateTask(task: Task) {
        val docRef = remoteDb.collection("users")
            .document(getUid())
            .collection("tasks")
            .document(task.id)

        docRef
            .update(
                "title", task.title,
                "description", task.description,
                "period", task.period,
                "color", task.colour
            )
            .addOnSuccessListener { Log.d(UPDATE_TAG, "Task count successfully updated!") }
            .addOnFailureListener { e -> Log.w(UPDATE_TAG, "Error updating document", e) }
    }

    fun updateHabit(habit: Habit) {
        val docRef = remoteDb.collection(USERS)
            .document(getUid())
            .collection(HABITS)
            .document(habit.id)

        docRef.update(
            "title", habit.title,
            "count", habit.count,
            "max", habit.max,
            "frequency", habit.frequency,
            "updatedAt", Calendar.getInstance().timeInMillis
        )
    }

    fun incrementPeriod() {
        _period.value?.let { periodTemp ->
            if (periodTemp <= 2) {
                _period.postValue(periodTemp + 1)
            }
        }
    }

    fun decrementPeriod() {
        _period.value?.let { periodTemp ->
            if (periodTemp >= 2) {
                _period.postValue(periodTemp - 1)
            }
        }
    }

    fun incrementFrequency() {
        _frequency.value?.let { freqTemp ->
            if (freqTemp <= 2) {
                _frequency.postValue(freqTemp + 1)
            }
        }
    }

    fun decrementFrequency() {
        _frequency.value?.let { freqTemp ->
            if (freqTemp >= 2) {
                _frequency.postValue(freqTemp - 1)
            }
        }
    }

    fun getUid(): String {
        currentUser?.let {
            val uid = it.uid
            Log.d("UID", uid.toString())
            return uid
        }
        return "1234"
    }


    fun updateOnComplete(task: Task) {
        uiScope.launch {
            val docRef =
                remoteDb.collection("users").document(getUid()).collection(TASKS).document(task.id)

            docRef
                .update(COMPLETED, task.completed)
                .addOnSuccessListener { Log.d(UPDATE_TAG, "Task successfully updated!") }
                .addOnFailureListener { e -> Log.w(UPDATE_TAG, "Error updating document", e) }
        }
    }

    fun deleteTask(task: Task) {
        uiScope.launch {
            remoteDb.collection("users").document(getUid()).collection(TASKS).document(task.id)
                .delete()
                .addOnCompleteListener {

                }
                .addOnFailureListener {

                }
        }
    }

    fun deleteHabit(habit: Habit) {
        uiScope.launch {
            remoteDb.collection(USERS).document(getUid()).collection(HABITS).document(habit.id)
                .delete()
                .addOnCompleteListener {

                }
                .addOnFailureListener {

                }
        }
    }

    fun addTask(task: Task) {
        uiScope.launch {
            val request =
                remoteDb.collection("users").document(getUid()).collection(TASKS).document()
            task.id = request.id
            request.set(task)
        }
    }

    fun addHabit(habit: Habit) {
        uiScope.launch {
            remoteDb.collection("users").document(getUid()).collection("habits").document(habit.id)
                .set(habit)
        }
    }

    fun deleteAllTasks() {
        uiScope.launch {

        }
    }


}