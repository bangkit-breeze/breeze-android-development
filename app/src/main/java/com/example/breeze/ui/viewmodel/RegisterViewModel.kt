package com.example.breeze.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.breeze.data.repository.UserRepository

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun register(fullname: String, email: String, password: String, confirmPassword: String) =
        userRepository.register(fullname, email, password, confirmPassword)
}