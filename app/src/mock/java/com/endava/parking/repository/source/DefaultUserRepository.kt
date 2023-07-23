package com.endava.parking.repository.source

import com.endava.parking.data.UserRepository
import com.endava.parking.data.model.User
import retrofit2.Response
import javax.inject.Inject

class DefaultUserRepository @Inject constructor() : UserRepository {

    private val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJJZCI6Ijg0MDQ1ZDU4LWYwZTktNDM0OC05NGQ3LTdlMTY1ZjY5MmUyMyIsIlVzZXJOYW1lIjoiQW5hdG9saWUiLCJFbWFpbCI6InN0YW1idWxvQHlhbmRleC5ydSIsIlJvbGUiOiJVc2VyIiwiaHR0cDovL3NjaGVtYXMubWljcm9zb2Z0LmNvbS93cy8yMDA4LzA2L2lkZW50aXR5L2NsYWltcy9yb2xlIjoiVXNlciIsImV4cCI6MTY5MTU3NzY0OCwiaXNzIjoiUGFya2luZ1BsYW5uZXIiLCJhdWQiOiJQYXJraW5Mb2dVSSJ9.jj_TjOkCASCkrQjkVKO9tdBStFdmdBkeUL68uA-l_V4"

    private val usersList = hashSetOf(
        User("U", "u@u.u","Ab123$", "12345678"),
        User("User1", "email1@mail.com", "Ab123456", "067599922"),
        User("User2", "email2@mail.com", "Ab123456", "067599922"),
        User("User3", "email3@mail.com", "Ab123456", "067599922"),
        User("Android", "android@gmail.com", "Android!1", "012345678")
    )

    override suspend fun signUp(user: User): Response<String> {
        usersList.add(user)
        return Response.success(user.name)
    }

    override suspend fun signIn(email: String, password: String): Response<String> {
        val response = usersList.filter { it.email == email }
        return if (response.isNotEmpty()) Response.success(200, token)
        else Response.success("Not found")
    }

    override suspend fun restorePassword(email: String): Response<String> = Response.success(email)
}
