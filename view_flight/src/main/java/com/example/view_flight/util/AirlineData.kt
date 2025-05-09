package com.example.view_flight.util

//данные об авиалинии
data class AirlineData(
    val airlineIata: String, //iata код авиалинии
    val flightNumber: String, //номер рейса в рамках авиалинии
    val airlineName: String //название авиалинии
)
