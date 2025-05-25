package com.example.network.models

import com.example.network.models.flight_schedule_subclasses.Flight
import com.example.network.models.flight_schedule_subclasses.FlightCodeshared
import com.example.network.models.flight_schedule_subclasses.FlightScheduleDestination
import com.example.network.models.flight_subclasses.FlightAirline
import com.google.gson.annotations.SerializedName

//ответ с API по рейсу
data class ResponseFlightSchedule(
    @SerializedName("airline") var airline: FlightAirline? = FlightAirline(),
    @SerializedName("arrival") var arrival: FlightScheduleDestination? = FlightScheduleDestination(),
    @SerializedName("codeshared") var codeshared: FlightCodeshared? = FlightCodeshared(),
    @SerializedName("departure") var departure: FlightScheduleDestination? = FlightScheduleDestination(),
    @SerializedName("flight") var flight: Flight? = Flight(),
    @SerializedName("status") var status: String? = null,
    @SerializedName("type") var type: String? = null
)
