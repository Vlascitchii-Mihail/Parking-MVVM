package com.endava.parking.di

import com.endava.parking.data.ParkingRepository
import com.endava.parking.data.UserRepository
import com.endava.parking.repository.source.DefaultParkingRepository
import com.endava.parking.repository.source.DefaultUserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(defaultUserRepository: DefaultUserRepository): UserRepository

    @Binds
    @Singleton
    abstract fun bindParkingRepository(defaultParkingRepository: DefaultParkingRepository): ParkingRepository
}
