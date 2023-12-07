package com.example.breeze.api

import com.example.breeze.data.model.auth.RegisterResponse
import com.example.breeze.data.model.auth.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse
}