package com.endava.parking.repository.source

import com.endava.parking.data.UserRepository
import com.endava.parking.data.model.User

class DefaultUserRepository : UserRepository {

    private val usersList = hashSetOf<User>(
        User("User1", "email1@mail.com", "Ab123456", "067599922"),
        User("User2", "email2@mail.com", "Ab123456", "067599922"),
        User("User3", "email3@mail.com", "Ab123456", "067599922")
    )

    override suspend fun signUp(user: User): Result<String> {
        usersList.add(user)
        return Result.success(user.name)
    }

    override suspend fun signIn(name: String, password: String): Result<String> {
        val result = usersList.single { s -> s.name == name }
        return Result.success(result.name)
    }

    override suspend fun restorePassword(email: String) = Result.success(email)
}
