package com.example.network.models.flight_schedule_subclasses

import com.google.gson.annotations.SerializedName

//данные о рейсе
data class Flight(
    @SerializedName("number") var number: String? = null, //номер рейса
    @SerializedName("iataNumber") var iataNumber: String? = null, //код iata
    @SerializedName("icaoNumber") var icaoNumber: String? = null //код icao
)
