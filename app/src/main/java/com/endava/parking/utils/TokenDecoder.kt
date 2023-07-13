package com.endava.parking.utils

import com.endava.parking.data.model.UserRole
import org.json.JSONObject
import java.util.Base64

fun getUserRole(token: String): UserRole {
    val parts = token.split(".")
    val charset = charset("UTF-8")
    val payload = String(Base64.getUrlDecoder().decode(parts[1].toByteArray(charset)), charset)

    val json = JSONObject(payload)
    val userRole = json.getString("Role")

    UserRole.values().forEach {
        if (it.role.equals(userRole, ignoreCase = true)) {
            return it

        }
    }

    return UserRole.INVALID
}
