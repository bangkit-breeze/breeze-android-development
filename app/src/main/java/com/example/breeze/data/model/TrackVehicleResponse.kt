package com.example.breeze.data.model

import com.google.gson.annotations.SerializedName

data class TrackVehicleResponse(

	@field:SerializedName("data")
	val dataTrackVehicle: DataTrackVehicle? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataTrackVehicle(

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("total_emission")
	val totalEmission: Int? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("reward_exp")
	val rewardExp: Int? = null
)
