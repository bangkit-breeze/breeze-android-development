package com.example.breeze.di

import android.app.Application
import com.example.breeze.api.ApiConfig
import com.example.breeze.data.local.datastore.UserPreferences
import com.example.breeze.data.local.datastore.dataStore
import com.example.breeze.data.repository.UserRepository

object Injection {
    private fun provideApiService() = ApiConfig.getApiService()

    fun provideUserRepository(application: Application): UserRepository {
        val pref = UserPreferences.getInstance(application.dataStore)
        return UserRepository.getInstance(provideApiService(), application, pref)
    }


}