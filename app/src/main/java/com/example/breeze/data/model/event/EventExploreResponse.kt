package com.example.breeze.data.model.event

import com.google.gson.annotations.SerializedName

data class EventResponse(

	@field:SerializedName("data")
	val dataEvent: DataEvent? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataEvent(

	@field:SerializedName("end_at")
	val endAt: String? = null,

	@field:SerializedName("highlighted")
	val highlighted: Boolean? = null,

	@field:SerializedName("event_image_url")
	val eventImageUrl: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("start_at")
	val startAt: String? = null,

	@field:SerializedName("reward_points")
	val rewardPoints: Int? = null,

	@field:SerializedName("location_lng")
	val locationLng: Any? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("creator_id")
	val creatorId: String? = null,

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("location_lat")
	val locationLat: Any? = null,

	@field:SerializedName("status")
	val status: String? = null
)
