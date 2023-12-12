package com.example.breeze.data.repository

import android.app.Application
import androidx.lifecycle.liveData
import com.example.breeze.R
import com.example.breeze.api.ApiService
import com.example.breeze.utils.ApiErrorUtils
import com.example.breeze.utils.AuthUtils
import retrofit2.HttpException
import java.io.IOException
import com.example.breeze.utils.constans.Result

class ArticleRepository private constructor(
    private val apiService: ApiService,
    private val application: Application
) {
    fun getStories(token: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getArticles(AuthUtils.getAuthToken(token))
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
        private var instance: ArticleRepository? = null
        fun getInstance(
            apiService: ApiService,
            application: Application
        ): ArticleRepository =
            instance ?: synchronized(this) {
                instance ?: ArticleRepository(apiService, application)
            }.also { instance = it }
    }
}