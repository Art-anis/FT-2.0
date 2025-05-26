package com.example.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.db.entities.TrackedFlightEntity

//dao для отслеживаемых рейсов
@Dao
interface TrackedFlightDao {

    //получение рейса по iata и дате вылета
    @Query("select * from tracked_flights where flight_iata = :iata and (estimated_departure = :date or scheduled_departure = :date)")
    suspend fun getFlightByIata(iata: String, date: Long): List<TrackedFlightEntity>

    //получение всех рейсов
    @Query("select * from tracked_flights where username = :username order by scheduled_departure")
    suspend fun getAllFlights(username: String): List<TrackedFlightEntity>

    //добавление рейса
    @Insert
    suspend fun trackFlight(flight: TrackedFlightEntity)

    //обновление рейса
    @Update
    suspend fun updateFlight(flight: TrackedFlightEntity)

    //удаление рейса
    @Query("delete from tracked_flights where flight_iata = :iata and estimated_departure = :date and username = :username")
    suspend fun deleteTrackedFlight(iata: String, date: Long, username: String)
}