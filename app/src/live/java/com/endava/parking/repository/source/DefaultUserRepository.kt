package com.endava.parking.repository.source

import com.endava.parking.data.UserRepository
import com.endava.parking.data.model.User
import javax.inject.Inject

/**
 * Live Repository, will be fulfilled in the future, with backend
 */
class DefaultUserRepository @Inject constructor() : UserRepository {

    override suspend fun signUp(user: User): Result<String> {
        TODO("Not yet implemented")
    }

    override suspend fun signIn(name: String, password: String): Result<String> {
        TODO("Not yet implemented")
    }

    override suspend fun restorePassword(email: String): Result<String> {
        TODO("Not yet implemented")
    }
}

