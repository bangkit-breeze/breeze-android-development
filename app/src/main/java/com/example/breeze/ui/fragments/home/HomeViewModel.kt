package com.example.breeze.ui.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.breeze.data.model.auth.LoginResult
import com.example.breeze.data.repository.ArticleRepository
import com.example.breeze.data.repository.UserRepository

class HomeViewModel(
    private val userRepository: UserRepository,
    private val articleRepository: ArticleRepository
) : ViewModel() {
    fun getUserLogin(): LiveData<LoginResult> =  userRepository.getSession()
    fun getArticles(token: String) = articleRepository.getStories(token)
    fun getProfile(token: String) = userRepository.getProfile(token)
}