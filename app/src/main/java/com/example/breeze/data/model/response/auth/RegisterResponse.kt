package com.example.breeze.data.model.response.auth

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class RegisterResponse(

    @field:SerializedName("data")
	val registerResult: RegisterResult,

    @field:SerializedName("success")
	val success: Boolean? = null,

    @field:SerializedName("message")
	val message: String? = null
)

@Parcelize
data class RegisterResult(

	@field:SerializedName("fullName")
	val fullName: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("email")
	val email: String? = null
): Parcelable
