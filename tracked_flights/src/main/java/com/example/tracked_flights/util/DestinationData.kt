package com.example.tracked_flights.util

data class DestinationData(
    val airportIata: String,
    val cityName: String,
    val terminal: String,
    val gate: String,
    val time: Long
)