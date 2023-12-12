package com.example.breeze.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.breeze.data.model.response.auth.LoginResult
import com.example.breeze.data.repository.UserRepository
import java.io.File

class DetailCarbonViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    fun getUserLogin(): LiveData<LoginResult> =  userRepository.getSession()
    fun getStatistic(token: String) = userRepository.getStatistic(token)
    fun getHistoryTrack(token: String) = userRepository.getHistoryTrack(token)
}