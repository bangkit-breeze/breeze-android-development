package com.example.breeze.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.breeze.data.model.response.auth.LoginResult
import com.example.breeze.data.repository.EventRepository
import com.example.breeze.data.repository.UserRepository
import java.io.File

class FormEventViewModel(
    private val userRepository: UserRepository,
    private val eventRepository: EventRepository
) : ViewModel() {
    fun getSession(): LiveData<LoginResult> =  userRepository.getSession()
    fun uploadEvidenceEvent(token: String, id: String, file: File, description: String) =
        eventRepository.uploadEvidenceEvent(token, id, file, description)

}