package com.example.breeze.data.repository

import android.app.Application
import com.example.breeze.api.ApiService
import com.example.breeze.data.local.datastore.UserPreferences

class UserRepository private constructor(
    private var apiService: ApiService,
    private val application: Application,
    private val userPref: UserPreferences
) {




    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            application: Application,
            pref: UserPreferences
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, application, pref)
            }.also { instance = it }
    }

}