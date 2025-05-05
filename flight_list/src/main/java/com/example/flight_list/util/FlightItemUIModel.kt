package com.example.flight_list.util

data class FlightItemUIModel(
    val flightNumber: String,
    val departureIata: String,
    val arrivalIata: String,
    val departureTime: String,
    val arrivalTime: String
)
