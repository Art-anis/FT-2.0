package com.example.ft.util

import com.example.flight_list.util.FlightItemUIModel
import com.example.tracked_flights.util.TrackedFlightUIModel
import com.example.view_flight.util.AirlineData
import com.example.view_flight.util.DestinationData
import com.example.view_flight.util.ViewFlightUIModel
import java.text.SimpleDateFormat
import java.util.Locale

//конвертация модели отслеживаемого рейса в модель списка
fun TrackedFlightUIModel.toFlightItemUIModel(): FlightItemUIModel {
    
    return FlightItemUIModel(
        flightNumber = this.flightNumber,
        departureIata = this.departure.airportIata,
        arrivalIata = this.arrival.airportIata,
        departureTime = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(this.departure.time),
        arrivalTime = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(this.arrival.time),
        tracked = true
    )
}

//конвертация модели отслеживаемого рейса в модель просмотра рейса
fun TrackedFlightUIModel.toViewFlightUIModel(): ViewFlightUIModel {

    //собираем данные о вылете
    val departure = DestinationData(
        cityName = this.departure.cityName,
        iata = this.departure.airportIata,
        time = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(this.departure.time),
        terminal = this.departure.terminal,
        gate = this.departure.gate
    )

    //собираем данные о прибытии
    val arrival = DestinationData(
        cityName = this.arrival.cityName,
        iata = this.arrival.airportIata,
        time = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(this.arrival.time),
        terminal = this.arrival.terminal,
        gate = this.arrival.gate
    )

    //собираем данные об авиалинии
    val airline = AirlineData(
        airlineIata = this.airline.airlineIata,
        flightNumber = this.airline.flightNumber,
        airlineName = this.airline.airlineName
    )

    //собираем данные о кодшеринге
    val codeshared = this.codeshared?.let {
        AirlineData(
            airlineIata = it.airlineIata,
            flightNumber = it.flightNumber,
            airlineName = it.airlineName
        )
    }

    return ViewFlightUIModel(
        flightNumber = this.flightNumber,
        departure = departure,
        arrival = arrival,
        airline = airline,
        codeshared = codeshared,
        status = this.status
    )
}