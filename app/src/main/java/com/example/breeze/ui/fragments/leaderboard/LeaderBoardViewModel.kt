package com.example.breeze.ui.fragments.leaderboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.breeze.data.model.auth.LoginResult
import com.example.breeze.data.repository.EventRepository
import com.example.breeze.data.repository.LeaderBoardRepository
import com.example.breeze.data.repository.UserRepository

class LeaderBoardViewModel(
    private val userRepository: UserRepository,
    private val leaderBoardRepository: LeaderBoardRepository
) : ViewModel() {
    fun getLeaderBordAllTime(token: String) = leaderBoardRepository.getLeaderBoardAllTime(token)
    fun getUserLogin(): LiveData<LoginResult> =  userRepository.getSession()
    fun getProfile(token: String) = userRepository.getProfile(token)

}