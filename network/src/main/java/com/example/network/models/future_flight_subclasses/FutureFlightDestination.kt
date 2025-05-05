package com.example.network.models.future_flight_subclasses

import com.google.gson.annotations.SerializedName

//данные об месте вылета или прибытия
data class FutureFlightDestination(
    @SerializedName("iataCode") var iataCode: String? = null, //код iata
    @SerializedName("icaoCode") var icaoCode: String? = null, //код icao
    @SerializedName("terminal") var terminal: String? = null, //терминал
    @SerializedName("gate") var gate: String? = null, //выход
    @SerializedName("scheduledTime") var scheduledTime: String? = null //время вылета/прибытия, согласно расписанию
)
