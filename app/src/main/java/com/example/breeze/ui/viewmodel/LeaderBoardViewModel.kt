package com.example.breeze.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.breeze.data.model.response.auth.LoginResult
import com.example.breeze.data.repository.EventRepository
import com.example.breeze.data.repository.LeaderBoardRepository
import com.example.breeze.data.repository.UserRepository

class LeaderBoardViewModel(
    private val userRepository: UserRepository,
    private val leaderBoardRepository: LeaderBoardRepository
) : ViewModel() {
    fun getLeaderBordAllTime(token: String) = leaderBoardRepository.getLeaderBoardAllTime(token)
    fun getLeaderBordWeekly(token: String) = leaderBoardRepository.getLeaderBoardWeekly(token)
    fun getSession(): LiveData<LoginResult> =  userRepository.getSession()
    fun getProfile(token: String) = userRepository.getProfile(token)
}