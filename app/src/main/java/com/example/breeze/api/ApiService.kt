package com.example.breeze.api

import com.example.breeze.data.model.ArticleResponse
import com.example.breeze.data.model.DataArticle
import com.example.breeze.data.model.auth.LoginRequest
import com.example.breeze.data.model.auth.LoginResponse
import com.example.breeze.data.model.auth.RegisterResponse
import com.example.breeze.data.model.auth.RegisterRequest
import com.example.breeze.data.model.event.EventResponse
import com.example.breeze.data.model.event.JoinEventResponse
import com.example.breeze.data.model.event.UploadEvidenceEventResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

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

    @GET("events")
    suspend fun getEventsExplore(
        @Header("Authorization") token: String
    ): EventResponse

    @GET("events")
    suspend fun getEventsJoined(
        @Header("Authorization") token: String,
        @Query("status") query: String = "joined"
    ): EventResponse

    @GET("events")
    suspend fun getEventsFinished(
        @Header("Authorization") token: String,
        @Query("status") query: String = "finished"
    ): EventResponse

    @POST("events/{id}/join")
    suspend fun joinEvent(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): JoinEventResponse

    @Multipart
    @POST("events/{id}/upload-evidence")
    suspend fun uploadEvidenceEvent(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Part("description") description: RequestBody,
        @Part file: MultipartBody.Part
    ): UploadEvidenceEventResponse
}