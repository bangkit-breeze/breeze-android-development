package com.example.breeze.ui.fragments.event

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.breeze.data.model.auth.LoginResult
import com.example.breeze.data.repository.ArticleRepository
import com.example.breeze.data.repository.EventRepository
import com.example.breeze.data.repository.UserRepository

class EventViewModel(
    private val userRepository: UserRepository,
    private val eventRepository: EventRepository
) : ViewModel() {
    fun getEventExplore(token: String) = eventRepository.getEventExplore(token)
    fun getUserLogin(): LiveData<LoginResult> =  userRepository.getSession()
}