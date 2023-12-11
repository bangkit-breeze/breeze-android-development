package com.example.breeze.data.model.response.track

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class TrackFoodResponse(

    @field:SerializedName("data")
	val dataTrackFood: DataTrackFood? = null,

    @field:SerializedName("success")
	val success: Boolean? = null,

    @field:SerializedName("message")
	val message: String? = null
)
@Parcelize
data class DataTrackFood(

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("predict_result")
	val predictResult: PredictResult? = null
) : Parcelable


@Parcelize
data class IngredientsItem(

	@field:SerializedName("bahan")
	val bahan: String? = null,

	@field:SerializedName("berat")
	val berat: String? = null,

	@field:SerializedName("carbon_footprint")
	val carbonFootprint: String? = null
): Parcelable

@Parcelize
data class PredictResult(

    @field:SerializedName("food_name")
	val foodName: String? = null,

    @field:SerializedName("total_emissions")
	val totalEmissions: String? = null,

    @field:SerializedName("filename")
	val filename: String? = null,

    @field:SerializedName("confidence")
	val confidence: String? = null,

    @field:SerializedName("name")
	val name: String? = null,

    @field:SerializedName("ingredients")
	val ingredients: List<IngredientsItem?>? = null,

    @field:SerializedName("version")
	val version: String? = null
): Parcelable
