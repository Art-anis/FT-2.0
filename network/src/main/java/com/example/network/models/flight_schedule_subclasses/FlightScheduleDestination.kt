package com.example.network.models.flight_schedule_subclasses

import com.google.gson.annotations.SerializedName

//данные о месте вылета/прибытия
data class FlightScheduleDestination(
    @SerializedName("actualRunway") var actualRunway: String? = null,
    @SerializedName("actualTime") var actualTime: String? = null,
    @SerializedName("baggage") var baggage: String? = null,
    @SerializedName("delay") var delay: String? = null,
    @SerializedName("estimatedRunway") var estimatedRunway: String? = null,
    @SerializedName("estimatedTime") var estimatedTime: String? = null,
    @SerializedName("gate") var gate: String? = null,
    @SerializedName("iataCode") var iataCode: String? = null,
    @SerializedName("icaoCode") var icaoCode: String? = null,
    @SerializedName("scheduledTime") var scheduledTime: String? = null,
    @SerializedName("terminal") var terminal: String? = null
)
