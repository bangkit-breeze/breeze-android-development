package com.example.breeze.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.breeze.data.model.auth.LoginResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token_user")
class UserPreferences private constructor(private val dataStore: DataStore<Preferences>) {
    private val userTokenKey = stringPreferencesKey(USER_TOKEN_KEY)

    fun getSession(): Flow<LoginResult> = dataStore.data.map {
        LoginResult(
            token = it[userTokenKey] ?: ""
        )
    }
    suspend fun saveSession(data: LoginResult) = dataStore.edit {
        with(it) {
            this[userTokenKey] = data.token
        }
    }

    suspend fun deleteSession() = dataStore.edit { it.clear() }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }

        private const val USER_TOKEN_KEY = "token"
    }
}