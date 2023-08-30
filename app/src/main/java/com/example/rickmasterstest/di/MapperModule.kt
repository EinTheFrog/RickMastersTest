package com.example.rickmasterstest.di

import com.example.rickmasterstest.model.mappers.CameraMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MapperModule {
    @Singleton
    @Provides
    fun provideCameraMapper() = CameraMapper()
}