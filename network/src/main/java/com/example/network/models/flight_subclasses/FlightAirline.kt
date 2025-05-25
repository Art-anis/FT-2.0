package com.example.network.models.flight_subclasses

import com.google.gson.annotations.SerializedName

//данные об авиалинии
data class FlightAirline(
    @SerializedName("name") var name: String? = null, //название
    @SerializedName("iataCode") var iataCode: String? = null, //код iata
    @SerializedName("icaoCode") var icaoCode: String? = null //код icao
)
