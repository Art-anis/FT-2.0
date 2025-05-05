package com.example.network.models.future_flight_subclasses

import com.google.gson.annotations.SerializedName

//данные о рейсе
data class FutureFlight(
    @SerializedName("number") var number: String? = null, //номер рейса
    @SerializedName("iataNumber") var iataNumber: String? = null, //код iata
    @SerializedName("icaoNumber") var icaoNumber: String? = null //код icao
)
