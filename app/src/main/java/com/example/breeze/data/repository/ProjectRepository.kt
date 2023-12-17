package com.example.breeze.data.repository

import android.app.Application
import androidx.lifecycle.liveData
import com.example.breeze.R
import com.example.breeze.api.ApiService
import com.example.breeze.utils.api.ApiErrorUtils
import com.example.breeze.utils.api.AuthUtils
import retrofit2.HttpException
import java.io.IOException
import com.example.breeze.utils.constans.Result

class ProjectRepository private constructor(
    private val apiService: ApiService,
    private val application: Application
) {
    fun getProjects(token: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getProjects(AuthUtils.getAuthToken(token))
            emit(Result.Success(response))
        }catch (e: HttpException) {
            emit(ApiErrorUtils.handleHttpException(e))
        } catch (exception: IOException) {
            emit(Result.Error(application.resources.getString(R.string.network_error_message)))
        } catch (exception: Exception) {
            emit(Result.Error(exception.message ?: application.resources.getString(R.string.unknown_error)))
        }
    }
    companion object {
        @Volatile
        private var instance: ProjectRepository? = null
        fun getInstance(
            apiService: ApiService,
            application: Application
        ): ProjectRepository =
            instance ?: synchronized(this) {
                instance ?: ProjectRepository(apiService, application)
            }.also { instance = it }
    }
}