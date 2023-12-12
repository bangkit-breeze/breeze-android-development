package com.example.breeze.ui.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.breeze.data.repository.UserRepository
import com.example.breeze.di.Injection
import com.example.breeze.ui.viewmodel.LoginViewModel
import com.example.breeze.ui.activities.register.RegisterViewModel

class AuthViewModelFactory private constructor(private val userRepository: UserRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>):  T =
        when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) ->
                LoginViewModel(userRepository) as T
            modelClass.isAssignableFrom(RegisterViewModel::class.java) ->
                RegisterViewModel(userRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    companion object {
        @Volatile
        private var instance: AuthViewModelFactory? = null
        fun getInstance(application: Application): AuthViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: AuthViewModelFactory(Injection.provideUserRepository(application))
            }.also { instance = it }
    }

}