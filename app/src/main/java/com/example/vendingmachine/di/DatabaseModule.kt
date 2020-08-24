package com.example.vendingmachine.di

import android.app.Application
import androidx.room.Room
import com.example.vendingmachine.data.persistence.HabitDao
import com.example.vendingmachine.data.persistence.TaskDao
import com.example.vendingmachine.data.persistence.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton
import com.example.vendingmachine.data.persistence.TaskDatabase.Companion.DATABASE_NAME
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application) : TaskDatabase {
        return Room
            .databaseBuilder(app, TaskDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideTaskDao(db : TaskDatabase) : TaskDao {
        return db.getTaskDao()
    }

    @Provides
    @Singleton
    fun provideHabitDao(db : TaskDatabase) : HabitDao{
        return db.getHabitDao()
    }

    @Provides
    @Singleton
    fun provideFirestore() : FirebaseFirestore{
        return Firebase.firestore
    }

    @Provides
    @Singleton
    fun provideFireAuth() : FirebaseAuth{
        return FirebaseAuth.getInstance()
    }

}