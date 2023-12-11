package com.example.breeze.api

import com.example.breeze.data.model.response.track.AddTrackFoodResponse
import com.example.breeze.data.model.response.article.ArticleResponse
import com.example.breeze.data.model.response.leaderboard.LeaderBoardResponse
import com.example.breeze.data.model.request.TrackFoodRequest
import com.example.breeze.data.model.response.track.TrackFoodResponse
import com.example.breeze.data.model.request.TrackVehicleRequest
import com.example.breeze.data.model.response.track.TrackVehicleResponse
import com.example.breeze.data.model.response.user.UserProfileResponse
import com.example.breeze.data.model.request.LoginRequest
import com.example.breeze.data.model.response.auth.LoginResponse
import com.example.breeze.data.model.response.auth.RegisterResponse
import com.example.breeze.data.model.request.RegisterRequest
import com.example.breeze.data.model.response.event.EventResponse
import com.example.breeze.data.model.response.event.JoinEventResponse
import com.example.breeze.data.model.response.event.UploadEvidenceEventResponse
import com.example.breeze.data.model.response.user.UserStatisticResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
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

    @GET("events/popular")
    suspend fun getEventsPopular(
        @Header("Authorization") token: String
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

    @GET("users/profile")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): UserProfileResponse

    @GET("users/statistic")
    suspend fun getStatistic(
        @Header("Authorization") token: String
    ): UserStatisticResponse

    @GET("leaderboard/alltime")
    suspend fun getLeaderboardAlltime(
        @Header("Authorization") token: String
    ): LeaderBoardResponse

    @GET("leaderboard/weekly")
    suspend fun getLeaderboardWeekly(
        @Header("Authorization") token: String
    ): LeaderBoardResponse

    @Headers("Content-Type: application/json")
    @POST("emission/tracking/vehicle")
    suspend fun addTrackEmissionVehicle(
        @Header("Authorization") token: String,
        @Body request: TrackVehicleRequest
    ): TrackVehicleResponse

    @Multipart
    @POST("emission/tracking/food/predict")
    suspend fun predictTrackEmissionFood(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part
    ): TrackFoodResponse

    @Headers("Content-Type: application/json")
    @POST("emission/tracking/food/add")
    suspend fun addTrackEmissionFood(
        @Header("Authorization") token: String,
        @Body request: TrackFoodRequest
    ): AddTrackFoodResponse
}