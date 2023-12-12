package com.example.breeze.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.breeze.data.model.response.auth.LoginResult
import com.example.breeze.data.repository.EventRepository
import com.example.breeze.data.repository.UserRepository

class DetailEventViewModel(
    private val userRepository: UserRepository,
    private val eventRepository: EventRepository
) : ViewModel() {
    fun getUserLogin(): LiveData<LoginResult> =  userRepository.getSession()
    fun joinEvent(token: String, id: String) = eventRepository.joinEvent(token, id)

}