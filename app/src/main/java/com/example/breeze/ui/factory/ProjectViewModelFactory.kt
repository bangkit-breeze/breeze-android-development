package com.example.breeze.ui.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.breeze.data.repository.LeaderBoardRepository
import com.example.breeze.data.repository.ProjectRepository
import com.example.breeze.data.repository.UserRepository
import com.example.breeze.di.Injection
import com.example.breeze.ui.viewmodel.LeaderBoardViewModel
import com.example.breeze.ui.viewmodel.ProjectViewModel

class ProjectViewModelFactory private constructor(
    private val userRepository: UserRepository,
    private val projectRepository: ProjectRepository
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>):T =
        when {
            modelClass.isAssignableFrom(ProjectViewModel::class.java) ->
                ProjectViewModel(userRepository, projectRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    companion object {
        @Volatile
        private var instance: ProjectViewModelFactory? = null
        fun getInstance(application: Application): ProjectViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ProjectViewModelFactory(
                    Injection.provideUserRepository(application),
                    Injection.provideProjectRepository(application))
            }.also { instance = it }
    }

}