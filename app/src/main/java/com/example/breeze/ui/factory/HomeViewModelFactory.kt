package com.example.breeze.ui.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.breeze.data.repository.ArticleRepository
import com.example.breeze.data.repository.EventRepository
import com.example.breeze.data.repository.UserRepository
import com.example.breeze.di.Injection
import com.example.breeze.ui.fragments.home.HomeViewModel

class HomeViewModelFactory private constructor(
    private val userRepository: UserRepository,
    private val articleRepository: ArticleRepository,
    private val eventRepository: EventRepository
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>):T =
        when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) ->
                HomeViewModel(userRepository, articleRepository, eventRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    companion object {
        @Volatile
        private var instance: HomeViewModelFactory? = null
        fun getInstance(application: Application): HomeViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: HomeViewModelFactory(
                    Injection.provideUserRepository(application),
                    Injection.provideArticleRepository(application),
                    Injection.provideEventRepository(application))
            }.also { instance = it }
    }

}