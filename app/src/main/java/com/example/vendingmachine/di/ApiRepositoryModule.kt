package com.example.vendingmachine.di

import com.example.vendingmachine.data.network.ApiService
import com.example.vendingmachine.data.repository.ApiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object ApiRepositoryModule {

    @Singleton
    @Provides
    fun provideApiRepository(apiService: ApiService) : ApiRepository{
        return ApiRepository(apiService)
    }

}