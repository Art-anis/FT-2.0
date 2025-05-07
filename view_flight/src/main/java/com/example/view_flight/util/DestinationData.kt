package com.example.view_flight.util

//данные о вылете/прибытии
data class DestinationData(
    val cityName: String, //название города
    val iata: String, //iata код
    val time: String, //время вылета/прибытия
    val terminal: String, //терминал
    val gate: String //выход
)
