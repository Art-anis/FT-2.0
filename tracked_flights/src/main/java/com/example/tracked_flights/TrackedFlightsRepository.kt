package com.example.tracked_flights

import com.example.db.dao.AirlineDao
import com.example.db.dao.AirportDao
import com.example.db.dao.CityDao
import com.example.db.dao.TrackedFlightDao
import com.example.db.entities.TrackedFlightEntity
import com.example.tracked_flights.util.AirlineData
import com.example.tracked_flights.util.DestinationData
import com.example.tracked_flights.util.TrackedFlightUIModel
import com.example.tracked_flights.util.toUIModel
import java.text.SimpleDateFormat
import java.util.Locale

class TrackedFlightsRepository(
    private val trackedFlightDao: TrackedFlightDao,
    private val airportDao: AirportDao,
    private val cityDao: CityDao,
    private val airlineDao: AirlineDao
) {

    suspend fun getAllTrackedFlights(): List<TrackedFlightUIModel> {
        val result = trackedFlightDao.getAllFlights()
        return result.mapNotNull {
            val departureCity = getCityName(it.departureIata.uppercase())
            val arrivalCity = getCityName(it.arrivalIata.uppercase())

            val airlineName = getAirlineName(it.airlineIata.uppercase())
            val codesharedName = getAirlineName(it.codesharedAirlineIata.uppercase())

            if (departureCity != null && arrivalCity != null && airlineName != null) {
                it.toUIModel(
                    departureCity = departureCity,
                    arrivalCity = arrivalCity,
                    airlineName = airlineName,
                    codesharedName = codesharedName
                )
            }
            else {
                null
            }
        }
    }

    suspend fun trackFlight(
        departure: DestinationData,
        arrival: DestinationData,
        mainAirline: AirlineData,
        codeshared: AirlineData?
    ) {

        val entity = TrackedFlightEntity(
            airlineIata = mainAirline.airlineIata,
            arrivalGate = arrival.gate,
            arrivalIata = arrival.airportIata,
            arrivalTerminal = arrival.terminal,
            scheduledDeparture = departure.time,
            codesharedAirlineIata = codeshared?.airlineIata ?: "",
            codesharedFlightIata = codeshared?.flightNumber ?: "",
            departureGate = departure.gate,
            departureIata = departure.airportIata,
            departureTerminal = departure.terminal,
            scheduledArrival = arrival.time,
            flightIata = mainAirline.flightNumber
        )

        trackedFlightDao.trackFlight(entity)
    }

    suspend fun getTrackedFlight(iata: String, date: Long): TrackedFlightUIModel? {
        val result = trackedFlightDao.getFlightByIata(iata, date)

        return result?.let {
            val departureCity = getCityName(it.departureIata.uppercase())
            val arrivalCity = getCityName(it.arrivalIata.uppercase())

            val airlineName = getAirlineName(it.airlineIata.uppercase())
            val codesharedName = getAirlineName(it.codesharedAirlineIata.uppercase())

            if (departureCity != null && arrivalCity != null && airlineName != null) {
                return it.toUIModel(
                    departureCity = departureCity,
                    arrivalCity = arrivalCity,
                    airlineName = airlineName,
                    codesharedName = codesharedName
                )
            }
            else {
                return null
            }
        }
    }

    private suspend fun getAirlineName(airlineIata: String): String? {
        val airline = airlineDao.getAirlineByIata(airlineIata)
        return airline?.name
    }

    private suspend fun getCityName(airportIata: String): String? {
        val airport = airportDao.getAirportByIata(airportIata)
        airport?.let {
            return cityDao.getCityByIata(it.cityIata)?.name
        } ?: return null
    }
}