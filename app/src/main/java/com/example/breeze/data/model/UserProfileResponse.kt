package com.example.breeze.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class UserProfileResponse(

	@field:SerializedName("data")
	val dataUser: DataUser? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

@Parcelize
data class DataUser(

	@field:SerializedName("food_emission_count")
	val foodEmissionCount: Int? = null,

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("total_co2_removed")
	val totalCo2Removed: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("experiences")
	val experiences: Int? = null,

	@field:SerializedName("vehicle_footprint_sum")
	val vehicleFootprintSum: Int? = null,

	@field:SerializedName("points")
	val points: Int? = null,

	@field:SerializedName("full_name")
	val fullName: String? = null,

	@field:SerializedName("avatar_url")
	val avatarUrl: String? = null,

	@field:SerializedName("food_footprint_sum")
	val foodFootprintSum: Int? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("vehicle_emission_count")
	val vehicleEmissionCount: Int? = null,

	@field:SerializedName("email")
	val email: String? = null
): Parcelable
