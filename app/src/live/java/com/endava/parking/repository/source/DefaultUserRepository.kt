package com.endava.parking.repository.source

import com.endava.parking.data.UserRepository
import com.endava.parking.data.api.ApiService
import com.endava.parking.data.model.User
import retrofit2.Response
import javax.inject.Inject

class DefaultUserRepository @Inject constructor(
    private val apiService: ApiService
) : UserRepository {

    override suspend fun signUp(user: User): Response<String> =
        apiService.signUpUser(user)

    override suspend fun signIn(email: String, password: String): Response<String> =
        apiService.signInUser(mapOf("email" to email, "password" to password))

    override suspend fun restorePassword(email: String): Response<String> =
        apiService.restorePassword(email)
}

