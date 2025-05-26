package com.example.network.models

import com.google.gson.annotations.SerializedName

data class ResponseNearbyAirport(
    @SerializedName("GMT") var gmt: String? = null,
    @SerializedName("codeIataAirport") var codeIataAirport: String? = null,
    @SerializedName("codeIataCity") var codeIataCity: String? = null,
    @SerializedName("codeIcaoAirport") var codeIcaoAirport: String? = null,
    @SerializedName("codeIso2Country") var codeIso2Country: String? = null,
    @SerializedName("distance") var distance: Double? = null,
    @SerializedName("latitudeAirport") var latitudeAirport: Double? = null,
    @SerializedName("longitudeAirport") var longitudeAirport: Double? = null,
    @SerializedName("nameAirport") var nameAirport: String? = null,
    @SerializedName("nameCountry") var nameCountry: String? = null,
    @SerializedName("phone") var phone: String? = null,
    @SerializedName("timezone") var timezone: String? = null
)
