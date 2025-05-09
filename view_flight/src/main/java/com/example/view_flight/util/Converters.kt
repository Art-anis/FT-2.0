package com.example.view_flight.util

import com.example.network.models.ResponseFutureFlight

//конвертирование ответа из api в UI-модель
fun ResponseFutureFlight.toUIModel(
    departureCityName: String,
    arrivalCityName: String
): ViewFlightUIModel {

    //собираем данные о месте вылета
    val departure = DestinationData(
        cityName = departureCityName,
        iata = this.departure?.iataCode ?: "",
        time = this.departure?.scheduledTime ?: "",
        terminal = this.departure?.terminal ?: "",
        gate = this.departure?.gate ?: ""
    )

    //также собираем аднные о месте прибытия
    val arrival = DestinationData(
        cityName = arrivalCityName,
        iata = this.arrival?.iataCode ?: "",
        time = this.arrival?.scheduledTime ?: "",
        terminal = this.arrival?.terminal ?: "",
        gate = this.arrival?.gate ?: ""
    )

    //основная авиалиния
    val mainAirline = AirlineData(
        airlineIata = this.airline?.iataCode ?: "",
        flightNumber = "${this.airline?.iataCode} ${this.flight?.number}".uppercase(),
        airlineName = this.airline?.name ?: ""
    )

    //кодшеринг, если есть
    val codeshared = this.codeshared?.let { codeshared ->
            codeshared.flight?.number?.let {
                AirlineData(
                    airlineIata = codeshared.airline?.iataCode ?: "",
                    flightNumber = "${codeshared.airline?.iataCode} ${codeshared.flight?.number}".uppercase(),
                    airlineName = codeshared.airline?.name ?: ""
                )
            }
    }

    //возвращаем собранную модель
    return ViewFlightUIModel(
        flightNumber = mainAirline.flightNumber,
        departure = departure,
        arrival = arrival,
        airline = mainAirline,
        codeshared = codeshared,
        aircraft = this.aircraft?.modelText ?: ""
    )
}