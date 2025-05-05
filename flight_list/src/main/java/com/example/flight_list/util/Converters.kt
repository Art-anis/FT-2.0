package com.example.flight_list.util

import com.example.network.models.ResponseFutureFlight

//конвертация ответа API в UI-модель
fun ResponseFutureFlight.toUIModel(): FlightItemUIModel {
    return FlightItemUIModel(
        flightNumber = "${this.airline?.iataCode} ${this.flight?.number}",
        departureIata = this.departure?.iataCode ?: "",
        arrivalIata = this.arrival?.iataCode ?: "",
        departureTime = this.departure?.scheduledTime ?: "",
        arrivalTime = this.arrival?.scheduledTime ?: ""
    )
}