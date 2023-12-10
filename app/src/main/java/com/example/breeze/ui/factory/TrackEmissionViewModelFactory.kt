package com.example.breeze.ui.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.breeze.data.repository.EventRepository
import com.example.breeze.data.repository.LeaderBoardRepository
import com.example.breeze.data.repository.TrackEmissionRepository
import com.example.breeze.data.repository.UserRepository
import com.example.breeze.di.Injection
import com.example.breeze.ui.activities.details.events.DetailEventViewModel
import com.example.breeze.ui.activities.details.events.FormEventViewModel
import com.example.breeze.ui.activities.vehicle.AddVehicleCarbonViewModel
import com.example.breeze.ui.fragments.event.EventViewModel
import com.example.breeze.ui.fragments.leaderboard.LeaderBoardViewModel

class TrackEmissionViewModelFactory private constructor(
    private val userRepository: UserRepository,
    private val trackEmissionRepository: TrackEmissionRepository
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>):T =
        when {
            modelClass.isAssignableFrom(AddVehicleCarbonViewModel::class.java) ->
                AddVehicleCarbonViewModel(userRepository, trackEmissionRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    companion object {
        @Volatile
        private var instance: TrackEmissionViewModelFactory? = null
        fun getInstance(application: Application): TrackEmissionViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: TrackEmissionViewModelFactory(
                    Injection.provideUserRepository(application),
                    Injection.provideTrackEmissionRepository(application))
            }.also { instance = it }
    }

}