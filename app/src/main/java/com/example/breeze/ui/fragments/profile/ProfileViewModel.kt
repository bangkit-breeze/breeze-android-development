package com.example.breeze.ui.fragments.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.breeze.data.model.response.auth.LoginResult
import com.example.breeze.data.repository.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    fun getUserLogin(): LiveData<LoginResult> =  userRepository.getSession()
    fun deleteUserLogin() = viewModelScope.launch { userRepository.deleteSession() }
    fun getProfile(token: String) = userRepository.getProfile(token)
}