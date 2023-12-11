package com.example.breeze.data.model.response.event

import com.google.gson.annotations.SerializedName

data class UploadEvidenceEventResponse(

	@field:SerializedName("data")
	val data: Any? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
