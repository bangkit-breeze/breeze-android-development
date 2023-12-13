package com.example.breeze.data.repository

import android.app.Application
import androidx.lifecycle.liveData
import com.example.breeze.R
import com.example.breeze.api.ApiService
import com.example.breeze.utils.api.ApiErrorUtils
import com.example.breeze.utils.api.AuthUtils
import com.example.breeze.utils.constans.Result
import retrofit2.HttpException
import java.io.IOException

class LeaderBoardRepository private constructor(
    private val apiService: ApiService,
    private val application: Application,
) {
    private suspend fun <T> apiCall(call: suspend () -> T) = try {
        Result.Success(call())
    } catch (e: HttpException) {
        Result.Error(ApiErrorUtils.handleHttpExceptionString(e))
    } catch (exception: IOException) {
        Result.Error(application.resources.getString(R.string.network_error_message))
    } catch (exception: Exception) {
        Result.Error(exception.message ?: application.resources.getString(R.string.unknown_error))
    }
    fun getLeaderBoardAllTime(token: String) = liveData {
        emit(Result.Loading)
        emit(apiCall { apiService.getLeaderboardAlltime(AuthUtils.getAuthToken(token)) })
    }
    fun getLeaderBoardWeekly(token: String) = liveData {
        emit(Result.Loading)
        emit(apiCall { apiService.getLeaderboardWeekly(AuthUtils.getAuthToken(token)) })
    }
    companion object {
        @Volatile
        private var instance: LeaderBoardRepository? = null
        fun getInstance(apiService: ApiService, application: Application): LeaderBoardRepository =
            instance ?: synchronized(this) {
                instance ?: LeaderBoardRepository(apiService, application)
            }.also { instance = it }
    }
}