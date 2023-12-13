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
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class EventRepository private constructor(
    private val apiService: ApiService,
    private val application: Application
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
    fun getEventExplore(token: String) = liveData {
        emit(Result.Loading)
        emit(apiCall { apiService.getEventsExplore(AuthUtils.getAuthToken(token)) })
    }
    fun getEventJoined(token: String) = liveData {
        emit(Result.Loading)
        emit(apiCall { apiService.getEventsJoined(AuthUtils.getAuthToken(token)) })
    }
    fun getEventFinished(token: String) = liveData {
        emit(Result.Loading)
        emit(apiCall { apiService.getEventsFinished(AuthUtils.getAuthToken(token)) })
    }
    fun getEventPopular(token: String) = liveData {
        emit(Result.Loading)
        emit(apiCall { apiService.getEventsPopular(AuthUtils.getAuthToken(token)) })
    }
    fun joinEvent(token: String, id: String) = liveData {
        emit(Result.Loading)
        emit(apiCall { apiService.joinEvent(AuthUtils.getAuthToken(token), id) })
    }
    fun uploadEvidenceEvent(token: String, id: String, file: File, description: String) = liveData {
        emit(Result.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData("file", file.name, requestImageFile)
        emit(apiCall { apiService.uploadEvidenceEvent(AuthUtils.getAuthToken(token), id, requestBody, multipartBody) })
    }
    companion object {
        @Volatile
        private var instance: EventRepository? = null
        fun getInstance(apiService: ApiService, application: Application): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(apiService, application)
            }.also { instance = it }
    }
}