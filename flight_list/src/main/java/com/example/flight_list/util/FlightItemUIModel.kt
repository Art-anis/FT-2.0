package com.example.flight_list.util

//UI-модель для элемента списка рейсов
data class FlightItemUIModel(
    val flightNumber: String,
    val departureIata: String,
    val arrivalIata: String,
    val departureTime: String,
    val arrivalTime: String,
    val tracked: Boolean
)
