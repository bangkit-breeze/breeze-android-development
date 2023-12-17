package com.example.breeze.di

import android.app.Application
import com.example.breeze.api.ApiConfig
import com.example.breeze.data.local.datastore.UserPreferences
import com.example.breeze.data.local.datastore.dataStore
import com.example.breeze.data.repository.ArticleRepository
import com.example.breeze.data.repository.EventRepository
import com.example.breeze.data.repository.LeaderBoardRepository
import com.example.breeze.data.repository.ProjectRepository
import com.example.breeze.data.repository.TrackEmissionRepository
import com.example.breeze.data.repository.UserRepository

object Injection {
    private fun provideApiService() = ApiConfig.getApiService()
    private fun provideUserPreferences(application: Application) =
        UserPreferences.getInstance(application.dataStore)
    fun provideUserRepository(application: Application) =
        UserRepository.getInstance(provideApiService(), application, provideUserPreferences(application))
    fun provideArticleRepository(application: Application) =
        ArticleRepository.getInstance(provideApiService(), application)
    fun provideProjectRepository(application: Application) =
        ProjectRepository.getInstance(provideApiService(), application)
    fun provideEventRepository(application: Application) =
        EventRepository.getInstance(provideApiService(), application)
    fun provideLeaderBoardRepository(application: Application) =
        LeaderBoardRepository.getInstance(provideApiService(), application)
    fun provideTrackEmissionRepository(application: Application) =
        TrackEmissionRepository.getInstance(provideApiService(), application)
}