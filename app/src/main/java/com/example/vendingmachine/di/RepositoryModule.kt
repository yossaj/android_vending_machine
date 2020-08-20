package com.example.vendingmachine.di

import com.example.vendingmachine.data.persistence.TaskDao
import com.example.vendingmachine.data.network.ApiService
import com.example.vendingmachine.data.persistence.HabitDao
import com.example.vendingmachine.data.repository.ApiRepository
import com.example.vendingmachine.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideApiRepository(apiService: ApiService) : ApiRepository{
        return ApiRepository(apiService)
    }

    @Singleton
    @Provides
    fun provideUserRepository(taskDao: TaskDao, habitDao: HabitDao) : UserRepository{
        return UserRepository(taskDao, habitDao)
    }

}