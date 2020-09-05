package com.example.vendingmachine.workers

import android.content.Context
import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.lifecycle.MutableLiveData
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.vendingmachine.R
import com.example.vendingmachine.data.models.Habit
import com.example.vendingmachine.utils.Constants
import com.example.vendingmachine.utils.Constants.HABITS
import com.example.vendingmachine.utils.Constants.USERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.util.*
import java.util.concurrent.CountDownLatch

class DailyHabitReset @WorkerInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val remoteDb: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : CoroutineWorker(context, params) {

    val uiScope = CoroutineScope(Dispatchers.IO)
    var registeredQuery = MutableLiveData<ListenerRegistration?>()
    val appContext = applicationContext

    override suspend fun doWork(): Result {

        try {
            val latch = CountDownLatch(1)
            val userID =  firebaseAuth.currentUser!!.uid
            Log.d("Habit", "UserID is ${userID}")
            val habitQuery = getQuery(userID)
            val listenerRegistration = prepareTasksRequiringUpdate(habitQuery, latch, userID)
            registeredQuery.postValue(listenerRegistration)
            latch.await()
            Log.d("Habit CHECK", "Latch Opened")
            listenerRegistration.remove()
            Log.d("Habit CHECK", "Listener Removed")
            return Result.success()
            Log.d("Habit CHECK", "Returned Success")
        } catch (throwable: Throwable) {
            Log.e(appContext.getString(R.string.habit_worker_error_tag), appContext.getString(R.string.habit_worker_error_message), throwable)
            return Result.failure()
        }
    }

    fun updateTask(habit: Habit, userID: String){
        val docRef = remoteDb.collection("users")
            .document(userID)
            .collection(HABITS)
            .document(habit.id)

        docRef
            .update(
                "updatedAt", habit.updatedAt,
                "count", habit.count
            )
            .addOnSuccessListener { Log.d(Constants.UPDATE_TAG, "Habit Count count successfully updated!") }
            .addOnFailureListener { e -> Log.w(Constants.UPDATE_TAG, "Error updating document", e) }
    }

    fun getQuery(userID: String): Query{
         return remoteDb.collection(USERS)
            .document(userID)
            .collection(HABITS)
            .whereEqualTo("frequency", 1)
    }

    fun prepareTasksRequiringUpdate(
        habitQuery: Query,
        latch: CountDownLatch,
        userID: String
    ) : ListenerRegistration {
        return habitQuery.addSnapshotListener { snapshot, exception ->
            exception?.message?.let {
                Log.d("FIRE EXCEPTION", it) }

            snapshot?.let {
                val remoteHabit: List<Habit> = it.toObjects(Habit::class.java)
                remoteHabit.forEach {
                    if(it.count != 0){
                        Log.d("Habit", it.title)
                        it.count = 0
                        it.updatedAt = System.currentTimeMillis()
                        updateTask(it, userID)
                    }
                }
                Log.d("Habit CHECK", "Loop has finished 1")
                latch.countDown()
            }
            Log.d("Habit CHECK", "Loop has finished 1")
        }
    }
}