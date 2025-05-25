package com.example.network.models.flight_schedule_subclasses

import com.example.network.models.flight_subclasses.FlightAirline
import com.google.gson.annotations.SerializedName

//данные о код-шеринге
data class FlightCodeshared(
    @SerializedName("airline") var airline: FlightAirline? = FlightAirline(), //авиалиния
    @SerializedName("flight") var flight: Flight? = Flight() //рейс
)
