package com.example.breeze.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.breeze.data.model.response.auth.LoginResult
import com.example.breeze.data.repository.ArticleRepository
import com.example.breeze.data.repository.EventRepository
import com.example.breeze.data.repository.UserRepository

class HomeViewModel(
    private val userRepository: UserRepository,
    private val articleRepository: ArticleRepository,
    private val eventRepository: EventRepository
) : ViewModel() {
    fun getToken(): LiveData<LoginResult> =  userRepository.getSession()
    fun getArticles(token: String) = articleRepository.getStories(token)
    fun getProfile(token: String) = userRepository.getProfile(token)
    fun getEventsPopular(token: String) = eventRepository.getEventPopular(token)

}