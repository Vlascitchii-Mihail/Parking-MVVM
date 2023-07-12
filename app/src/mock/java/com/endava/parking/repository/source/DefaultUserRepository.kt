package com.endava.parking.repository.source

import com.endava.parking.data.UserRepository
import com.endava.parking.data.model.User
import retrofit2.Response
import javax.inject.Inject

class DefaultUserRepository @Inject constructor() : UserRepository {

    private val usersList = arrayListOf<User>(
        User("User1", "email1@mail.com", "Ab123456", "067599922"),
        User("User2", "email2@mail.com", "Ab123456", "067599922"),
        User("User3", "email3@mail.com", "Ab123456", "067599922")
    )

    override suspend fun signUp(user: User): Response<String> {
        usersList.add(user)
        return Response.success("success")
    }

    override suspend fun signIn(email: String, password: String): Response<String> {
        return Response.success(200, "token")
    }

    override suspend fun restorePassword(email: String): Response<String> = Response.success("true")
}
