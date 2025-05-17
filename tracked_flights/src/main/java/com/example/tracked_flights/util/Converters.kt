package com.example.tracked_flights.util

import com.example.db.entities.TrackedFlightEntity

fun TrackedFlightEntity.toUIModel(
    departureCity: String,
    arrivalCity: String,
    airlineName: String,
    codesharedName: String?
): TrackedFlightUIModel {
    val departure = DestinationData(
        airportIata = this.departureIata,
        cityName = departureCity,
        terminal = this.departureTerminal,
        gate = this.departureGate,
        time = if (this.estimatedDeparture != 0L && this.estimatedDeparture != this.scheduledDeparture)
            this.estimatedDeparture else this.scheduledDeparture
    )

    val arrival = DestinationData(
        airportIata = this.arrivalIata,
        cityName = arrivalCity,
        terminal = this.arrivalTerminal,
        gate = this.arrivalGate,
        time = if (this.estimatedArrival != 0L && this.estimatedArrival != this.scheduledArrival)
            this.estimatedArrival else this.scheduledArrival
    )

    val airline = AirlineData(
        airlineIata = this.airlineIata,
        flightNumber = this.flightIata,
        airlineName = airlineName
    )

    val codeshared = codesharedName?.let {
        AirlineData(
            airlineIata = this.codesharedAirlineIata,
            flightNumber = this.codesharedFlightIata,
            airlineName = it
        )
    }

    return TrackedFlightUIModel(
        flightNumber = this.flightIata,
        departure = departure,
        arrival = arrival,
        airline = airline,
        codeshared = codeshared,
    )
}