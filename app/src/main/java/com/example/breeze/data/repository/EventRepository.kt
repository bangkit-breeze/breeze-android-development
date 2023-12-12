package com.example.breeze.data.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.liveData
import com.example.breeze.R
import com.example.breeze.api.ApiService
import com.example.breeze.data.model.response.ErrorResponse
import retrofit2.HttpException
import java.io.IOException
import com.example.breeze.utils.constans.Result
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class EventRepository private constructor(
    private val apiService: ApiService,
    private val application: Application
) {

    fun getEventExplore(token: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getEventsExplore("Bearer $token")
            emit(Result.Success(response))
        }catch (e: HttpException) {
            emit(handleHttpException(e))
        } catch (exception: IOException) {
            emit(Result.Error(application.resources.getString(R.string.network_error_message)))
        } catch (exception: Exception) {
            emit(Result.Error(exception.message ?: application.resources.getString(R.string.unknown_error)))
        }
    }

    fun getEventJoined(token: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getEventsJoined("Bearer $token")
            emit(Result.Success(response))
        }catch (e: HttpException) {
            emit(handleHttpException(e))
        } catch (exception: IOException) {
            emit(Result.Error(application.resources.getString(R.string.network_error_message)))
        } catch (exception: Exception) {
            emit(Result.Error(exception.message ?: application.resources.getString(R.string.unknown_error)))
        }
    }

    fun getEventFinished(token: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getEventsFinished("Bearer $token")
            emit(Result.Success(response))
        }catch (e: HttpException) {
            emit(handleHttpException(e))
        } catch (exception: IOException) {
            emit(Result.Error(application.resources.getString(R.string.network_error_message)))
        } catch (exception: Exception) {
            emit(Result.Error(exception.message ?: application.resources.getString(R.string.unknown_error)))
        }
    }

    fun getEventPopular(token: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getEventsPopular("Bearer $token")
            emit(Result.Success(response))
        }catch (e: HttpException) {
            emit(handleHttpException(e))
        } catch (exception: IOException) {
            emit(Result.Error(application.resources.getString(R.string.network_error_message)))
        } catch (exception: Exception) {
            emit(Result.Error(exception.message ?: application.resources.getString(R.string.unknown_error)))
        }
    }

    fun joinEvent(token: String, id: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.joinEvent("Bearer $token", id)
            emit(Result.Success(response))
        }catch (e: HttpException) {
            emit(handleHttpException(e))
        } catch (exception: IOException) {
            emit(Result.Error(application.resources.getString(R.string.network_error_message)))
        } catch (exception: Exception) {
            emit(Result.Error(exception.message ?: application.resources.getString(R.string.unknown_error)))
        }
    }

    fun uploadEvidenceEvent( token: String, id: String, file: File, description: String )= liveData {
        emit(Result.Loading)
        val authToken  = "Bearer $token"
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "file",
            file.name,
            requestImageFile
        )
        try {
            val response = apiService.uploadEvidenceEvent(authToken , id, requestBody,  multipartBody )
            emit(Result.Success(response))
        } catch (e: HttpException) {
            emit(handleHttpException(e))
        } catch (exception: IOException) {
            emit(Result.Error(application.resources.getString(R.string.network_error_message)))
        } catch (exception: Exception) {
            emit(Result.Error(exception.message ?: application.resources.getString(R.string.unknown_error)))
        }
    }
    private fun handleHttpException(exception: HttpException): Result.Error {
        val jsonInString = exception.response()?.errorBody()?.string()
        Log.e("Error Response", jsonInString ?: "Empty error response")
        val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
        val errorMessage = errorBody.message
        return Result.Error(errorMessage)
    }

    private fun getAuthToken(token: String): String {
        return "Bearer $token"
    }

    companion object {
        @Volatile
        private var instance: EventRepository? = null
        fun getInstance(
            apiService: ApiService,
            application: Application
        ): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(apiService, application)
            }.also { instance = it }
    }
}