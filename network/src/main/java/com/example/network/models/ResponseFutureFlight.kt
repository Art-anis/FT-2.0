package com.example.network.models

import com.example.network.models.future_flight_subclasses.FutureFlight
import com.example.network.models.future_flight_subclasses.FutureFlightAircraft
import com.example.network.models.future_flight_subclasses.FutureFlightAirline
import com.example.network.models.future_flight_subclasses.FutureFlightCodeshared
import com.example.network.models.future_flight_subclasses.FutureFlightDestination
import com.google.gson.annotations.SerializedName

//ответ с API рейсов
data class ResponseFutureFlight(
    @SerializedName("weekday") var weekday: String? = null, //день недели
    @SerializedName("departure") var departure: FutureFlightDestination? = FutureFlightDestination(), //данные о месте вылета
    @SerializedName("arrival") var arrival: FutureFlightDestination? = FutureFlightDestination(), //данные о месте прибытия
    @SerializedName("aircraft") var aircraft: FutureFlightAircraft? = FutureFlightAircraft(), //данные о судне
    @SerializedName("airline") var airline: FutureFlightAirline? = FutureFlightAirline(), //данные об авиалинии
    @SerializedName("flight") var flight: FutureFlight? = FutureFlight(), //данные о рейсе
    @SerializedName("codeshared") var codeshared: FutureFlightCodeshared? = FutureFlightCodeshared() //данные о код-шеринге
)