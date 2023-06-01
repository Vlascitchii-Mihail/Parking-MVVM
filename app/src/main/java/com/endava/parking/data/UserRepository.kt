package com.endava.parking.data

import com.endava.parking.data.model.User

interface UserRepository {

    suspend fun signUp(user: User): Result<String>

    suspend fun signIn(name: String, password: String): Result<String>

    suspend fun restorePassword(email: String): Result<String>
}
