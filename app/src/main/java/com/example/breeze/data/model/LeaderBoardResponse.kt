package com.example.breeze.data.model

import com.google.gson.annotations.SerializedName

data class LeaderBoardResponse(

	@field:SerializedName("data")
	val dataLeaderboard: Data? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Data(

	@field:SerializedName("leaderboard")
	val leaderboards: List<LeaderboardItem?>? = null,

	@field:SerializedName("userRank")
	val userRank: Int? = null
)

data class LeaderboardItem(

	@field:SerializedName("full_name")
	val fullName: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("avatar_url")
	val avatarUrl: String? = null,

	@field:SerializedName("rank")
	val rank: Int? = null,

	@field:SerializedName("exp")
	val exp: Int? = null
)
