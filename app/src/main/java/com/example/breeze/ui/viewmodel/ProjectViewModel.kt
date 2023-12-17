package com.example.breeze.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.breeze.data.model.response.auth.LoginResult
import com.example.breeze.data.repository.ArticleRepository
import com.example.breeze.data.repository.EventRepository
import com.example.breeze.data.repository.ProjectRepository
import com.example.breeze.data.repository.UserRepository

class ProjectViewModel(
    private val userRepository: UserRepository,
    private val projectRepository: ProjectRepository
) : ViewModel() {
    fun getProject(token: String) = projectRepository.getProjects(token)
    fun getSession(): LiveData<LoginResult> =  userRepository.getSession()

}