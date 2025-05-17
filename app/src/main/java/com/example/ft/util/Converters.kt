package com.example.ft.util

import com.example.db.entities.TrackedFlightEntity
import com.example.flight_list.util.FlightItemUIModel
import com.example.tracked_flights.util.TrackedFlightUIModel
import com.example.view_flight.util.AirlineData
import com.example.view_flight.util.DestinationData
import com.example.view_flight.util.ViewFlightUIModel
import java.text.SimpleDateFormat
import java.util.Locale

fun TrackedFlightUIModel.toFlightItemUIModel(): FlightItemUIModel {
    
    return FlightItemUIModel(
        flightNumber = this.flightNumber,
        departureIata = this.departure.airportIata,
        arrivalIata = this.arrival.airportIata,
        departureTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(this.departure.time),
        arrivalTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(this.arrival.time)
    )
}

fun TrackedFlightUIModel.toViewFlightUIModel(): ViewFlightUIModel {

    val departure = DestinationData(
        cityName = this.departure.cityName,
        iata = this.departure.airportIata,
        time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(this.departure.time),
        terminal = this.departure.terminal,
        gate = this.departure.gate
    )

    val arrival = DestinationData(
        cityName = this.arrival.cityName,
        iata = this.arrival.airportIata,
        time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(this.arrival.time),
        terminal = this.arrival.terminal,
        gate = this.arrival.gate
    )

    val airline = AirlineData(
        airlineIata = this.airline.airlineIata,
        flightNumber = this.airline.flightNumber,
        airlineName = this.airline.airlineName
    )

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
        codeshared = codeshared
    )
}