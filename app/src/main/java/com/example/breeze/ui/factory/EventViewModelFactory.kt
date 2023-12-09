package com.example.breeze.ui.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.breeze.data.repository.ArticleRepository
import com.example.breeze.data.repository.EventRepository
import com.example.breeze.data.repository.UserRepository
import com.example.breeze.di.Injection
import com.example.breeze.ui.activities.register.RegisterViewModel
import com.example.breeze.ui.fragments.event.EventViewModel
import com.example.breeze.ui.fragments.home.HomeViewModel

class EventViewModelFactory private constructor(
    private val userRepository: UserRepository,
    private val eventRepository: EventRepository
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>):T =
        when {
            modelClass.isAssignableFrom(EventViewModel::class.java) ->
                EventViewModel(userRepository, eventRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    companion object {
        @Volatile
        private var instance: EventViewModelFactory? = null
        fun getInstance(application: Application): EventViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: EventViewModelFactory(
                    Injection.provideUserRepository(application),
                    Injection.provideEventRepository(application))
            }.also { instance = it }
    }

}