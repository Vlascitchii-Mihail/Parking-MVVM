package com.endava.parking.data

import com.endava.parking.data.model.User
import retrofit2.Response

interface UserRepository {

    suspend fun signUp(user: User): Response<String>

    suspend fun signIn(email: String, password: String): Response<String>

    suspend fun restorePassword(email: String): Response<String>
}
