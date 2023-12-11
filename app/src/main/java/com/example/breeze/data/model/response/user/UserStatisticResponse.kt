package com.example.breeze.data.model.response.user

import com.google.gson.annotations.SerializedName

data class UserStatisticResponse(

	@field:SerializedName("data")
	val dataUserStatistic: DataUserStatistic? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataUserStatistic(

	@field:SerializedName("foodEmissionCount")
	val foodEmissionCount: Int? = null,

	@field:SerializedName("vehicleFootprintSum")
	val vehicleFootprintSum: Int? = null,

	@field:SerializedName("foodEmissionPercentage")
	val foodEmissionPercentage: Any? = null,

	@field:SerializedName("vehicleEmissionPercentage")
	val vehicleEmissionPercentage: Any? = null,

	@field:SerializedName("foodFootprintSum")
	val foodFootprintSum: Int? = null,

	@field:SerializedName("vehicleEmissionCount")
	val vehicleEmissionCount: Int? = null
)
