package com.example.breeze.ui.activities.camera

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.breeze.data.model.response.auth.LoginResult
import com.example.breeze.data.repository.TrackEmissionRepository
import com.example.breeze.data.repository.UserRepository
import java.io.File

class AddFoodCarbonViewModel(
    private val userRepository: UserRepository,
    private val trackEmissionRepository: TrackEmissionRepository
) : ViewModel() {

    fun predictTrackEmissionFoodCarbon(token: String, file: File)
            = trackEmissionRepository.predictTrackEmissionFood(token, file)
    fun addTrackEmissionFood(token: String, name: String, emision: Int)
            = trackEmissionRepository.addTrackEmissionFood(token, name, emision)
    fun getUserLogin(): LiveData<LoginResult> =  userRepository.getSession()

}