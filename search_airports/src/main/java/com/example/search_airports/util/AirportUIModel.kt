package com.example.search_airports.util

//UI-модель аэропорта
data class AirportUIModel(
    val airportName: String = "",
    val cityName: String = "",
    val countryName: String = "",
    val iataCode: String = ""
)
