package com.example.rickmasterstest.di

import com.example.rickmasterstest.domain.HouseRepository
import com.example.rickmasterstest.domain.HouseRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindHouseRepository(impl: HouseRepositoryImpl): HouseRepository
}