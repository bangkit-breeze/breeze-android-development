package com.example.breeze.ui.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.breeze.data.repository.EventRepository
import com.example.breeze.data.repository.UserRepository
import com.example.breeze.di.Injection
import com.example.breeze.ui.viewmodel.DetailEventViewModel
import com.example.breeze.ui.viewmodel.FormEventViewModel
import com.example.breeze.ui.viewmodel.EventViewModel

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
            modelClass.isAssignableFrom(DetailEventViewModel::class.java) ->
                DetailEventViewModel(userRepository, eventRepository) as T
            modelClass.isAssignableFrom(FormEventViewModel::class.java) ->
                FormEventViewModel(userRepository, eventRepository) as T
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