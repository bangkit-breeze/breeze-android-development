package com.example.breeze.ui.activities.vehicle

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.breeze.data.model.auth.LoginResult
import com.example.breeze.data.repository.LeaderBoardRepository
import com.example.breeze.data.repository.TrackEmissionRepository
import com.example.breeze.data.repository.UserRepository


class AddVehicleCarbonViewModel(
    private val userRepository: UserRepository,
    private val trackEmissionRepository: TrackEmissionRepository
) : ViewModel() {
    fun getUserLogin(): LiveData<LoginResult> =  userRepository.getSession()

}