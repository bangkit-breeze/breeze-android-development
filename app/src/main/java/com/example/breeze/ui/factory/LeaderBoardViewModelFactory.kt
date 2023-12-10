package com.example.breeze.ui.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.breeze.data.repository.EventRepository
import com.example.breeze.data.repository.LeaderBoardRepository
import com.example.breeze.data.repository.UserRepository
import com.example.breeze.di.Injection
import com.example.breeze.ui.activities.details.events.DetailEventViewModel
import com.example.breeze.ui.activities.details.events.FormEventViewModel
import com.example.breeze.ui.fragments.event.EventViewModel
import com.example.breeze.ui.fragments.leaderboard.LeaderBoardViewModel

class LeaderBoardViewModelFactory private constructor(
    private val userRepository: UserRepository,
    private val leaderBoardRepository: LeaderBoardRepository
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>):T =
        when {
            modelClass.isAssignableFrom(LeaderBoardViewModel::class.java) ->
                LeaderBoardViewModel(userRepository, leaderBoardRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    companion object {
        @Volatile
        private var instance: LeaderBoardViewModelFactory? = null
        fun getInstance(application: Application): LeaderBoardViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: LeaderBoardViewModelFactory(
                    Injection.provideUserRepository(application),
                    Injection.provideLeaderBoardRepository(application))
            }.also { instance = it }
    }

}