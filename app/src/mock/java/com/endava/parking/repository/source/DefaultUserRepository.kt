package com.endava.parking.repository.source

import com.endava.parking.data.UserRepository
import com.endava.parking.data.model.User
import retrofit2.Response
import javax.inject.Inject

class DefaultUserRepository @Inject constructor() : UserRepository {

    private val usersList = hashSetOf<User>(
        User("User1", "email1@mail.com", "Ab123456", "067599922"),
        User("User2", "email2@mail.com", "Ab123456", "067599922"),
        User("User3", "email3@mail.com", "Ab123456", "067599922"),
        User("Android", "android@gmail.com", "Android!1", "012345678")
    )

    override suspend fun signUp(user: User): Response<String> {
        usersList.add(user)
        return Response.success(user.name)
    }

    override suspend fun signIn(name: String, password: String): Response<String> {
        val response = usersList.filter { it.name == name }
        return if (response.isNotEmpty()) Response.success(response.first().name)
        else Response.success("Not found")
    }

    override suspend fun restorePassword(email: String) = Response.success(email)
}
