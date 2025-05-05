package com.example.network.models.future_flight_subclasses

import com.google.gson.annotations.SerializedName

//данные о самолете
data class FutureFlightAircraft(
    @SerializedName("modelCode") var modelCode: String? = null, //код модели
    @SerializedName("modelText") var modelText: String? = null //текстовое название модели
)