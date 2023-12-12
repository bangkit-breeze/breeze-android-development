package com.example.breeze.ui.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.breeze.data.repository.ArticleRepository
import com.example.breeze.data.repository.EventRepository
import com.example.breeze.data.repository.UserRepository
import com.example.breeze.di.Injection
import com.example.breeze.ui.activities.details.carbon.DetailCarbonViewModel
import com.example.breeze.ui.activities.register.RegisterViewModel
import com.example.breeze.ui.fragments.event.EventViewModel
import com.example.breeze.ui.fragments.home.HomeViewModel
import com.example.breeze.ui.fragments.profile.ProfileViewModel

class ProfileViewModelFactory private constructor(
    private val userRepository: UserRepository
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>):T =
        when {
            modelClass.isAssignableFrom(ProfileViewModel::class.java) ->
                ProfileViewModel(userRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    companion object {
        @Volatile
        private var instance: ProfileViewModelFactory? = null
        fun getInstance(application: Application): ProfileViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ProfileViewModelFactory(
                    Injection.provideUserRepository(application)
                )
            }.also { instance = it }
    }

}