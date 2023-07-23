package com.endava.parking.di

import com.endava.parking.utils.EmailValidator
import com.endava.parking.utils.LotDetailsValidator
import com.endava.parking.utils.LotLevelsValidator
import com.endava.parking.utils.NameValidator
import com.endava.parking.utils.PasswordValidator
import com.endava.parking.utils.PhoneValidator
import com.endava.parking.utils.Validator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
abstract class ValidatorsModule {

    @Binds
    @Named("Name")
    abstract fun bindNameValidator(nameValidator: NameValidator): Validator

    @Binds
    @Named("Email")
    abstract fun bindEmailValidator(emailValidator: EmailValidator): Validator

    @Binds
    @Named("Password")
    abstract fun bindPasswordValidator(passwordValidator: PasswordValidator): Validator

    @Binds
    @Named("Phone")
    abstract fun bindPhoneValidator(phoneValidator: PhoneValidator): Validator

    @Binds
    @Named("LotDetails")
    abstract fun bindLotDetailsValidator(lotDetailsValidator: LotDetailsValidator): Validator

    @Binds
    @Named("LevelsCapacity")
    abstract fun bindLotLevelsCapacityValidator(lotLevelsCapacityValidator: LotLevelsValidator): Validator
}
