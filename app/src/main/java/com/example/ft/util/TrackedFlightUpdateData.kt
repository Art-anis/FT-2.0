package com.example.ft.util

//данные о обновлении рейса
data class TrackedFlightUpdateData(
    val flightNumber: String, //номер рейса
    val departure: Pair<String, String>, //вылет
    val arrival: Pair<String, String>, //прибытие
    val departureTime: Long //время вылета
)
