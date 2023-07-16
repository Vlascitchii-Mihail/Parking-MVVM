package com.endava.parking.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject

private const val USER_AUTH = "user-role"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = USER_AUTH)

class DefaultAuthDataStore @Inject constructor(private val context: Context): AuthDataStore {

    override suspend fun putUserRole(userRole: String) {
        val preferenceKey = stringPreferencesKey(KEY_USER_ROLE)
        context.dataStore.edit { preferences ->
            preferences[preferenceKey] = userRole
        }
    }

    override suspend fun getUserRole(): String? {
        val preferenceKey = stringPreferencesKey(KEY_USER_ROLE)
        val preferences = context.dataStore.data.first()
        return preferences[preferenceKey]
    }

    override suspend fun putAuthToken(token: String) {
        val preferenceKey = stringPreferencesKey(KEY_AUTH_TOKEN)
        context.dataStore.edit { preferences ->
            preferences[preferenceKey] = token
        }
    }

    override suspend fun getAuthToken(): String? {
        val preferenceKey = stringPreferencesKey(KEY_AUTH_TOKEN)
        val preferences = context.dataStore.data.first()
        return preferences[preferenceKey]
    }

    companion object {
        private const val KEY_USER_ROLE = "user_role"
        private const val KEY_AUTH_TOKEN = "auth_token"
        private const val KEY_PARKING_DETAILS = "parking_details"
    }
}
