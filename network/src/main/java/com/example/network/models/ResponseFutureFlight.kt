package com.example.network.models

import com.example.network.models.flight_schedule_subclasses.Flight
import com.example.network.models.future_flight_subclasses.FutureFlightAircraft
import com.example.network.models.flight_subclasses.FlightAirline
import com.example.network.models.flight_schedule_subclasses.FlightCodeshared
import com.example.network.models.future_flight_subclasses.FutureFlightDestination
import com.google.gson.annotations.SerializedName

//ответ с API рейсов
data class ResponseFutureFlight(
    @SerializedName("weekday") var weekday: String? = null, //день недели
    @SerializedName("departure") var departure: FutureFlightDestination? = FutureFlightDestination(), //данные о месте вылета
    @SerializedName("arrival") var arrival: FutureFlightDestination? = FutureFlightDestination(), //данные о месте прибытия
    @SerializedName("aircraft") var aircraft: FutureFlightAircraft? = FutureFlightAircraft(), //данные о судне
    @SerializedName("airline") var airline: FlightAirline? = FlightAirline(), //данные об авиалинии
    @SerializedName("flight") var flight: Flight? = Flight(), //данные о рейсе
    @SerializedName("codeshared") var codeshared: FlightCodeshared? = FlightCodeshared() //данные о код-шеринге
)