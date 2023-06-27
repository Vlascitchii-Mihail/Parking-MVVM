package com.endava.parking.di

import com.endava.parking.data.UserRepository
import com.endava.parking.repository.source.DefaultUserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UserRepositoryModule {

    @Singleton
    @Provides
    fun provideUserRepository(): UserRepository = DefaultUserRepository()
}
