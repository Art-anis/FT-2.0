package com.example.network.models.future_flight_subclasses

import com.google.gson.annotations.SerializedName

//данные о код-шеринге
data class FutureFlightCodeshared(
    @SerializedName("airline") var airline: FutureFlightAirline? = FutureFlightAirline(), //авиалиния
    @SerializedName("flight") var flight: FutureFlight? = FutureFlight() //рейс
)
