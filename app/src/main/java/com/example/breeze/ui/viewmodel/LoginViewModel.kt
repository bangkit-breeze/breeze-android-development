package com.example.breeze.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.breeze.data.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun login(email: String, password: String) = userRepository.login(email, password)
    fun getUserLogin() = userRepository.getSession()
    fun deleteUserLogin() = viewModelScope.launch { userRepository.deleteSession() }

}