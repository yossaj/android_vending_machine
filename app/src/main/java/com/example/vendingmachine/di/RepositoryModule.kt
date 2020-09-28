package com.example.vendingmachine.di

import com.example.vendingmachine.data.network.ApiService
import com.example.vendingmachine.data.repository.ApiRepository
import com.example.vendingmachine.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
    fun provideUserRepository(remoteDb : FirebaseFirestore, firebaseAuth : FirebaseAuth) : UserRepository{
        return UserRepository(remoteDb, firebaseAuth)
    }

}