package com.example.rickmasterstest.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.Realm
import io.realm.RealmConfiguration
import javax.inject.Singleton

const val REALM_NAME = "RickMastersTestProject"

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {
    @Singleton
    @Provides
    fun provideRealm(): Realm {
        val config = RealmConfiguration
            .Builder()
            .name(REALM_NAME)
            .schemaVersion(2)
            .deleteRealmIfMigrationNeeded()
            .build()
        return Realm.getInstance(config)
    }
}