package com.example.breeze.utils.api

import com.example.breeze.data.model.response.ErrorResponse
import retrofit2.HttpException
import com.example.breeze.utils.constans.Result
import com.google.gson.Gson

object ApiErrorUtils {
    fun handleHttpException(exception: HttpException): Result.Error {
        val jsonInString = exception.response()?.errorBody()?.string()
        val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
        val errorMessage = errorBody.message
        return Result.Error(errorMessage)
    }
    fun handleHttpExceptionString(exception: HttpException): String {
        val jsonInString = exception.response()?.errorBody()?.string()
        val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
        return errorBody.message ?: "Unknown error"
    }
}