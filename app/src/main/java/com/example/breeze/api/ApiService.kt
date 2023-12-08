package com.example.breeze.api

import com.example.breeze.data.model.ArticleResponse
import com.example.breeze.data.model.DataArticle
import com.example.breeze.data.model.auth.LoginRequest
import com.example.breeze.data.model.auth.LoginResponse
import com.example.breeze.data.model.auth.RegisterResponse
import com.example.breeze.data.model.auth.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @Headers("Content-Type: application/json")
    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("articles")
    suspend fun getArticles(
        @Header("Authorization") token: String
    ): ArticleResponse
}