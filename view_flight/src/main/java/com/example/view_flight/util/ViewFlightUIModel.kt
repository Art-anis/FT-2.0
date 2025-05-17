package com.example.view_flight.util

//UI-модель рейса
data class ViewFlightUIModel(
    val flightNumber: String, //номер рейса
    val departure: DestinationData, //данные о месте вылета
    val arrival: DestinationData, //данные о месте прибытия
    val airline: AirlineData, //данные об основной авиалинии
    val codeshared: AirlineData?, //данные о кодшеринге, если есть
)
