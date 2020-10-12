package com.venderino.vendingmachine.di

import com.venderino.vendingmachine.data.network.ApiService
import com.venderino.vendingmachine.data.repository.ApiRepository
import com.venderino.vendingmachine.data.repository.UserRepository
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