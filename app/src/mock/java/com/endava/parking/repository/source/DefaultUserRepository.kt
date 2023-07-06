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
        return try {
            Response.success("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJJZCI6Ijk3Yzg0YmRkLTNjZTQtNDljOS1hNzYxLWRhYzhlZWZjZmFkNCIsIlVzZXJOYW1lIjoiYW5kcm9pZCIsIkVtYWlsIjoiYW5kcm9pZEBnbWFpbC5jb20iLCJSb2xlIjoiVXNlciIsImh0dHA6Ly9zY2hlbWFzLm1pY3Jvc29mdC5jb20vd3MvMjAwOC8wNi9pZGVudGl0eS9jbGFpbXMvcm9sZSI6IlVzZXIiLCJleHAiOjE2OTE1ODgzMDksImlzcyI6IlBhcmtpbmdQbGFubmVyIiwiYXVkIjoiUGFya2luTG9nVUkifQ.NdIZdhSHDVZfsMf-1Ojyy2PI4vS_usDJ7N9-Ycjb4lQ ")
        } catch (ex: NoSuchElementException) {
            Response.success("Fail")
        }
    }

    override suspend fun restorePassword(email: String) = Response.success(email)
}
