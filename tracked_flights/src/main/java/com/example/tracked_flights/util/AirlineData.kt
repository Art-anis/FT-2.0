package com.example.tracked_flights.util

data class AirlineData(
    val airlineIata: String, //iata код авиалинии
    val flightNumber: String, //номер рейса в рамках авиалинии
    val airlineName: String //название авиалинии
)
