package com.example.breeze.data.repository

import android.app.Application
import androidx.lifecycle.liveData
import com.example.breeze.R
import com.example.breeze.api.ApiService
import com.example.breeze.data.local.datastore.UserPreferences
import com.example.breeze.data.model.ErrorResponse
import com.example.breeze.data.model.auth.RegisterRequest
import com.example.breeze.utils.Result
import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException

class UserRepository private constructor(
    private var apiService: ApiService,
    private val application: Application,
    private val userPref: UserPreferences
) {

    fun register(fullname:String, email:String, password:String, confirmPassword:String) = liveData {
        emit(Result.Loading)

        try {
            val response = apiService.register(RegisterRequest(fullname, email, password, confirmPassword))
            emit(Result.Success(response))
        }catch (e: HttpException) {
            emit(handleHttpException(e))
        }catch (exception: IOException) {
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