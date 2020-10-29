package com.venderino.vendingmachine.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.venderino.vendingmachine.data.models.Habit
import com.venderino.vendingmachine.data.models.Task
import com.venderino.vendingmachine.utils.Constants.COLOUR
import com.venderino.vendingmachine.utils.Constants.COMPLETED
import com.venderino.vendingmachine.utils.Constants.COUNT
import com.venderino.vendingmachine.utils.Constants.DESCRIPTION
import com.venderino.vendingmachine.utils.Constants.HABITS
import com.venderino.vendingmachine.utils.Constants.MAX
import com.venderino.vendingmachine.utils.Constants.PERIOD
import com.venderino.vendingmachine.utils.Constants.TASKS
import com.venderino.vendingmachine.utils.Constants.TITLE
import com.venderino.vendingmachine.utils.Constants.UPDATED_AT
import com.venderino.vendingmachine.utils.Constants.UPDATE_TAG
import com.venderino.vendingmachine.utils.Constants.USERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.venderino.vendingmachine.utils.Constants.CREATED_AT
import kotlinx.coroutines.*
import java.util.*

class UserRepository constructor(
    private val remoteDb: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) {

    private val currentUser = firebaseAuth.currentUser

    private var registeredTaskQuery = MutableLiveData<ListenerRegistration?>()
    private var registeredHabitQuery = MutableLiveData<ListenerRegistration?>()

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

    val _habitPeriod = MutableLiveData<Int>(1)
    val habitPeriod: LiveData<Int>
        get() = _habitPeriod

    val _coins = MutableLiveData<Int>(0)
    val coins : LiveData<Int>
        get() = _coins



    fun listenForTaskChanges() {

        uiScope.launch {
            val taskQuery = remoteDb.collection(USERS)
                .document(getUid())
                .collection(TASKS)
                .orderBy(UPDATED_AT, Query.Direction.DESCENDING)
                .whereEqualTo(PERIOD, period.value)

            registeredTaskQuery.postValue(
                taskQuery.addSnapshotListener { snapshot, exception ->
                    exception?.let {
                        Log.d("Exception", it.message)
                    }

                    snapshot?.let {
                        val remoteTasks: MutableList<Task> = mutableListOf<Task>()
                        for (document in it.documents) {
                            val task = Task(
                                document.getString(TITLE) as String,
                                document.getString(DESCRIPTION) as String,
                                document.get(PERIOD).toString().toInt(),
                                document.get(COLOUR).toString().toInt(),
                                document.get(COMPLETED) as Boolean,
                                document.get(UPDATED_AT) as Long,
                                document.get(CREATED_AT) as Long,
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
                .whereEqualTo(PERIOD, habitPeriod.value)
                .orderBy(UPDATED_AT, Query.Direction.ASCENDING)

            registeredHabitQuery.postValue(
                habitQuery.addSnapshotListener { snapshot, exception ->
                    exception?.let {
                        Log.d("Habit Except", it.message)
                    }

                    snapshot?.let {
                        var remoteHabits: MutableList<Habit> = mutableListOf<Habit>()
                        for (document in it.documents) {
                            val habit = Habit(
                                document.getString(TITLE) as String,
                                document.get(MAX).toString().toInt(),
                                document.get(PERIOD).toString().toInt(),
                                document.get(COUNT).toString().toInt(),
                                document.get(UPDATED_AT) as Long,
                                document.get(CREATED_AT) as Long,
                                document.id
                            )
                            remoteHabits.add(habit)
                            Log.d("Doc", "Document id added: ${document.id} - ${habit.id}")
                        }
                        Log.d("Habit Doc", "Habit list size ${remoteHabits.size}")
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
        habit.updatedAt = System.currentTimeMillis()
        val docRef = remoteDb.collection(USERS)
            .document(getUid())
            .collection(HABITS)
            .document(habit.id)

        docRef
            .update(
                COUNT, count,
                UPDATED_AT, habit.updatedAt
            )
            .addOnSuccessListener { Log.d(UPDATE_TAG, "Habit count successfully updated!") }
            .addOnFailureListener { e -> Log.w(UPDATE_TAG, "Error updating document", e) }
    }

    fun updateTask(task: Task) {
        task.updatedAt = System.currentTimeMillis()
        val docRef = remoteDb.collection(USERS)
            .document(getUid())
            .collection(TASKS)
            .document(task.id)

        docRef
            .update(
                TITLE, task.title,
                DESCRIPTION, task.description,
                PERIOD, task.period,
                COLOUR, task.colour,
                UPDATED_AT, task.updatedAt
            )
            .addOnSuccessListener { Log.d(UPDATE_TAG, "Task count successfully updated!") }
            .addOnFailureListener { e -> Log.w(UPDATE_TAG, "Error updating document", e) }
    }

    fun updateHabit(habit: Habit) {
        habit.updatedAt = System.currentTimeMillis()

        val docRef = remoteDb.collection(USERS)
            .document(getUid())
            .collection(HABITS)
            .document(habit.id)

        docRef.update(
            TITLE, habit.title,
            COUNT, habit.count,
            MAX, habit.max,
            PERIOD, habit.period,
            UPDATED_AT, habit.updatedAt
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
        _habitPeriod.value?.let { freqTemp ->
            if (freqTemp <= 2) {
                _habitPeriod.postValue(freqTemp + 1)
            }
        }
    }

    fun decrementFrequency() {
        _habitPeriod.value?.let { freqTemp ->
            if (freqTemp >= 2) {
                _habitPeriod.postValue(freqTemp - 1)
            }
        }
    }

    private fun getUid(): String {
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
                remoteDb.collection(USERS).document(getUid()).collection(TASKS).document(task.id)

            docRef
                .update(
                    COMPLETED, task.completed,
                    UPDATED_AT, System.currentTimeMillis())
                .addOnSuccessListener { Log.d(UPDATE_TAG, "Task successfully updated!") }
                .addOnFailureListener { e -> Log.w(UPDATE_TAG, "Error updating document", e) }
        }
    }

    fun deleteTask(task: Task) {
        uiScope.launch {
            remoteDb.collection(USERS).document(getUid()).collection(TASKS).document(task.id)
                .delete()
                .addOnCompleteListener {
                    Log.d("Task", "Task has been deleted.")
                }
                .addOnFailureListener {
                    Log.d("Task", "Task has been deleted.")

                }
        }
    }

    fun deleteHabit(habit: Habit) {
        uiScope.launch {
            remoteDb.collection(USERS).document(getUid()).collection(HABITS).document(habit.id)
                .delete()
                .addOnCompleteListener {
                    Log.d("Habit", "Habit has been deleted.")
                }
                .addOnFailureListener {
                    Log.d("Habit", "Failed to delete habit.")
                }
        }
    }

    fun addTask(task: Task) {
        uiScope.launch {
            val request =
                remoteDb.collection(USERS).document(getUid()).collection(TASKS).document()
            task.id = request.id
            request.set(task)
        }
    }

    fun addHabit(habit: Habit) {
        uiScope.launch {
            val request = remoteDb.collection(USERS).document(getUid()).collection(HABITS).document()
            habit.id = request.id
            request.set(habit)
        }
    }

    fun getTokenCount(){
        val TAG  = "COINS"
        val docRef = remoteDb.collection(USERS).document(getUid())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val tokenCount = document.get("tokenTotal") as Long;
                    _coins.postValue(tokenToCoins(tokenCount));
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
    }

    fun reduceTokenCount(){
        coins.value?.let {
            var tokenCount = coinsToTokens(it)
            Log.d("COINS", "Current token Count $tokenCount")
            tokenCount -= 10
            Log.d("COINS", "Incremented Count $tokenCount")
            val docRef = remoteDb.collection(USERS).document(getUid())
            docRef.update("tokenTotal" , tokenCount)
            getTokenCount()
        }
    }

    fun increaseTokenCount(){
        coins.value?.let {
            var tokenCount = coinsToTokens(it)
            Log.d("COINS", "Current token Count $tokenCount")
            tokenCount +=10
            Log.d("COINS", "Incremented Count $tokenCount")
            val docRef = remoteDb.collection(USERS).document(getUid())
            docRef.update("tokenTotal" , tokenCount)
            getTokenCount()
        }
    }

    fun  tokenToCoins(tokens : Long) : Int{
        return (tokens/10).toInt()
    }

    fun coinsToTokens(coinCount: Int) : Long{
        return (coinCount.times(10)).toLong()
    }

}