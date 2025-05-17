package com.example.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.db.entities.TrackedFlightEntity

@Dao
interface TrackedFlightDao {

    @Query("select * from tracked_flights where flight_iata = :iata and scheduled_departure = :date")
    suspend fun getFlightByIata(iata: String, date: Long): TrackedFlightEntity?

    @Query("select * from tracked_flights")
    suspend fun getAllFlights(): List<TrackedFlightEntity>

    @Insert
    suspend fun trackFlight(flight: TrackedFlightEntity)
}