package com.endava.parking.data.model

enum class UserRole(val role: String) {
    ADMIN("Admin"),
    REGULAR("User"),
    INVALID("Invalid");

    companion object {
        fun getFromString(str: String?): UserRole {
            return values().firstOrNull { str == it.role } ?: INVALID
        }
    }
}
