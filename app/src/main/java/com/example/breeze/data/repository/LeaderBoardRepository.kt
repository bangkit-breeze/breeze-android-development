package com.example.breeze.data.repository

import android.app.Application
import androidx.lifecycle.liveData
import com.example.breeze.R
import com.example.breeze.api.ApiService
import com.example.breeze.data.model.ErrorResponse
import com.example.breeze.utils.Result
import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException

class LeaderBoardRepository private constructor(
    private val apiService: ApiService,
    private val application: Application,
) {

    fun getLeaderBoardAllTime(token: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getLeaderboardAlltime("Bearer $token")
            emit(Result.Success(response))
        }catch (e: HttpException) {
            emit(handleHttpException(e))
        } catch (exception: IOException) {
            emit(Result.Error(application.resources.getString(R.string.network_error_message)))
        } catch (exception: Exception) {
            emit(Result.Error(exception.message ?: application.resources.getString(R.string.unknown_error)))
        }
    }

    private fun handleHttpException(exception: HttpException): Result.Error {
        val jsonInString = exception.response()?.errorBody()?.string()
        val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
        val errorMessage = errorBody.message
        return Result.Error(errorMessage)
    }

    private fun getAuthToken(token: String): String {
        return "Bearer $token"
    }

    companion object {
        @Volatile
        private var instance: LeaderBoardRepository? = null
        fun getInstance(
            apiService: ApiService,
            application: Application
        ): LeaderBoardRepository =
            instance ?: synchronized(this) {
                instance ?: LeaderBoardRepository(apiService, application)
            }.also { instance = it }
    }
}