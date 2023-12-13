package com.example.breeze.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.example.breeze.R
import com.example.breeze.api.ApiService
import com.example.breeze.data.local.datastore.UserPreferences
import com.example.breeze.data.model.request.auth.LoginRequest
import com.example.breeze.data.model.response.auth.LoginResult
import com.example.breeze.data.model.request.auth.RegisterRequest
import com.example.breeze.utils.api.ApiErrorUtils
import com.example.breeze.utils.api.AuthUtils
import com.example.breeze.utils.constans.Result
import retrofit2.HttpException
import java.io.IOException

class UserRepository private constructor(
    private val apiService: ApiService,
    private val application: Application,
    private val userPref: UserPreferences
) {
    private suspend fun <T> apiCall(call: suspend () -> T): Result<T> = try {
        Result.Success(call())
    } catch (e: HttpException) {
        Result.Error(ApiErrorUtils.handleHttpExceptionString(e))
    } catch (exception: IOException) {
        Result.Error(application.resources.getString(R.string.network_error_message))
    } catch (exception: Exception) {
        Result.Error(exception.message ?: application.resources.getString(R.string.unknown_error))
    }
    fun register(fullname: String, email: String, password: String, confirmPassword: String) = liveData {
        emit(Result.Loading)
        emit(apiCall { apiService.register(RegisterRequest(fullname, email, password, confirmPassword)) })
    }
    fun login(email: String, password: String) = liveData {
        emit(Result.Loading)
        emit(apiCall {
            val response = apiService.login(LoginRequest(email, password))
            saveSession(response.loginResult)
            response
        })
    }
    fun getProfile(token: String) = liveData {
        emit(Result.Loading)
        emit(apiCall { apiService.getProfile(AuthUtils.getAuthToken(token)) })
    }
    fun getStatistic(token: String) = liveData {
        emit(Result.Loading)
        emit(apiCall { apiService.getStatistic(AuthUtils.getAuthToken(token)) })
    }
    fun getHistoryTrack(token: String) = liveData {
        emit(Result.Loading)
        emit(apiCall { apiService.getHistoryTrack(AuthUtils.getAuthToken(token)) })
    }
    suspend fun saveSession(data: LoginResult) = userPref.saveSession(data)
    fun getSession(): LiveData<LoginResult> = userPref.getSession().asLiveData()
    suspend fun deleteSession() = userPref.deleteSession()
    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            application: Application,
            pref: UserPreferences
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, application, pref)
            }.also { instance = it }
    }
}