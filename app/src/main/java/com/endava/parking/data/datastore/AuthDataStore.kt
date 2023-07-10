package com.endava.parking.data.datastore

interface AuthDataStore {

    suspend fun putUserRole(userRole: String)

    suspend fun getUserRole(): String?

    suspend fun putAuthToken(token: String)

    suspend fun getAuthToken(): String?
}
