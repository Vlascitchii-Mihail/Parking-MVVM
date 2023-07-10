package com.endava.parking.di

import android.content.Context
import com.endava.parking.data.datastore.AuthDataStore
import com.endava.parking.data.datastore.DefaultAuthDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Singleton
    @Provides
    fun provideDataStoreRepository(@ApplicationContext app: Context): AuthDataStore = DefaultAuthDataStore(app)
}
