package com.example.breeze.data.repository

import android.app.Application
import androidx.lifecycle.liveData
import com.example.breeze.R
import com.example.breeze.api.ApiService
import com.example.breeze.data.model.response.ErrorResponse
import com.example.breeze.data.model.request.track.TrackFoodRequest
import com.example.breeze.data.model.request.track.TrackVehicleRequest
import com.example.breeze.utils.AuthUtils
import com.example.breeze.utils.constans.Result
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.IOException

class TrackEmissionRepository private constructor(
    private val apiService: ApiService,
    private val application: Application,
) {
    private suspend fun <T> apiCall(call: suspend () -> T): Result<T> = try {
        Result.Success(call())
    } catch (e: HttpException) {
        handleHttpException(e)
    } catch (exception: IOException) {
        Result.Error(application.resources.getString(R.string.network_error_message))
    }  catch (exception: Exception) {
        Result.Error(exception.message ?: application.resources.getString(R.string.unknown_error))
    }
    fun addTrackEmissionVehicle(token: String, vehicleType: String, distance: Int) = liveData {
        emit(Result.Loading)
        emit(apiCall {
            apiService.addTrackEmissionVehicle(
                AuthUtils.getAuthToken(token),
                TrackVehicleRequest(vehicleType, distance)
            )
        })
    }
    fun addTrackEmissionFood(token: String, foodName: String, totalEmission: Int) = liveData {
        emit(Result.Loading)
        emit(apiCall {
            apiService.addTrackEmissionFood(
                AuthUtils.getAuthToken(token),
                TrackFoodRequest(foodName, totalEmission)
            )
        })
    }
    fun predictTrackEmissionFood(token: String, file: File) = liveData {
        emit(Result.Loading)
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "file",
            file.name,
            requestImageFile
        )
        try {
            val response = apiService.predictTrackEmissionFood(AuthUtils.getAuthToken(token), multipartBody)
            emit(Result.Success(response))
        }catch (e: HttpException) {
            emit(handleHttpException(e))
        } catch (exception: IOException) {
            emit(Result.Error(application.resources.getString(R.string.track_error_message)))
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
    companion object {
        @Volatile
        private var instance: TrackEmissionRepository? = null
        fun getInstance(apiService: ApiService, application: Application): TrackEmissionRepository =
            instance ?: synchronized(this) {
                instance ?: TrackEmissionRepository(apiService, application)
            }.also { instance = it }
    }
}