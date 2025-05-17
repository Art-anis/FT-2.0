package com.example.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "tracked_flights", indices = [Index(value = ["scheduled_departure", "flight_iata"], unique = true)])
data class TrackedFlightEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id") val id: Int = 0,
    @ColumnInfo("airline_iata") val airlineIata: String,
    @ColumnInfo("scheduled_arrival", defaultValue = "0") val scheduledArrival: Long = 0,
    @ColumnInfo("estimated_arrival", defaultValue = "0") val estimatedArrival: Long = 0,
    @ColumnInfo("baggage") val baggage: String = "",
    @ColumnInfo("arrival_delay") val arrivalDelay: Int = 0,
    @ColumnInfo("arrival_gate") val arrivalGate: String,
    @ColumnInfo("arrival_iata") val arrivalIata: String,
    @ColumnInfo("arrival_terminal") val arrivalTerminal: String,
    @ColumnInfo("codeshared_airline_iata") val codesharedAirlineIata: String,
    @ColumnInfo("codeshared_flight_iata") val codesharedFlightIata: String,
    @ColumnInfo("scheduled_departure", defaultValue = "0") val scheduledDeparture: Long = 0,
    @ColumnInfo("estimated_departure", defaultValue = "0") val estimatedDeparture: Long = 0,
    @ColumnInfo("departure_delay") val departureDelay: Int = 0,
    @ColumnInfo("departure_gate") val departureGate: String,
    @ColumnInfo("departure_iata") val departureIata: String,
    @ColumnInfo("departure_terminal") val departureTerminal: String,
    @ColumnInfo("flight_iata") val flightIata: String,
    @ColumnInfo("status") val status: String = ""
)
