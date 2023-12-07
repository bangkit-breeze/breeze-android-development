package com.example.breeze.data.model.auth

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class LoginResponse(

	@field:SerializedName("data")
	val loginResult: LoginResult,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

@Parcelize
data class LoginResult(

	@field:SerializedName("token")
	val token: String? = null
): Parcelable
