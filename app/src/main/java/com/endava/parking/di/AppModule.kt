package com.endava.parking.di

import com.endava.parking.utils.EmailValidator
import com.endava.parking.utils.PasswordValidator
import com.endava.parking.utils.Validator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Named("email")
    fun provideEmailValidator(): Validator = EmailValidator()

    @Provides
    @Named("password")
    fun providePassValidation(): Validator = PasswordValidator()
}
