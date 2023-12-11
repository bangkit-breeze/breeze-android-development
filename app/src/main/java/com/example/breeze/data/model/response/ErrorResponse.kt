package com.example.breeze.data.model.response

import com.google.gson.annotations.SerializedName

data class ErrorResponse(

	@field:SerializedName("data")
	val data: Any? = null,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)
