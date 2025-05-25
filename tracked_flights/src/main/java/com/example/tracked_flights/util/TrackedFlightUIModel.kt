package com.example.tracked_flights.util

data class TrackedFlightUIModel(
    val flightNumber: String,
    val departure: DestinationData,
    val arrival: DestinationData,
    val airline: AirlineData,
    val codeshared: AirlineData?,
    val status: String
)
