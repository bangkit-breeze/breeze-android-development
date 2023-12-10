package com.example.breeze.ui.activities.camera

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.breeze.data.model.auth.LoginResult
import com.example.breeze.data.repository.TrackEmissionRepository
import com.example.breeze.data.repository.UserRepository
import java.io.File

class AddFoodCarbonViewModel(
    private val userRepository: UserRepository,
    private val trackEmissionRepository: TrackEmissionRepository
) : ViewModel() {

    fun addTrackEmissionFoodCarbon(token: String, file: File)
            = trackEmissionRepository.addTrackEmissionFood(token, file)
    fun getUserLogin(): LiveData<LoginResult> =  userRepository.getSession()

}