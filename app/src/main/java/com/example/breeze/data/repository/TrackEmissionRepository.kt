package com.example.breeze.data.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.liveData
import com.example.breeze.R
import com.example.breeze.api.ApiService
import com.example.breeze.data.model.ErrorResponse
import com.example.breeze.data.model.TrackVehicleRequest
import com.example.breeze.data.model.auth.RegisterRequest
import com.example.breeze.utils.Result
import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException

class TrackEmissionRepository private constructor(
    private val apiService: ApiService,
    private val application: Application,
) {

    fun addTrackEmissionVehicle(token: String, vehicleType: String, distance: Int) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.addTrackEmissionVehicle("Bearer $token", TrackVehicleRequest(vehicleType, distance))
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
        private var instance: TrackEmissionRepository? = null
        fun getInstance(
            apiService: ApiService,
            application: Application
        ): TrackEmissionRepository =
            instance ?: synchronized(this) {
                instance ?: TrackEmissionRepository(apiService, application)
            }.also { instance = it }
    }
}