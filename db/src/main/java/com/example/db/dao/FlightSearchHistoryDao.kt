package com.example.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.db.entities.FlightSearchHistoryEntity

@Dao
interface FlightSearchHistoryDao {

    @Query("select * from flight_search_history order by searchTime desc limit 5")
    suspend fun getLastSearchedFlights(): List<FlightSearchHistoryEntity>

    @Insert
    suspend fun addFlightToHistory(flight: FlightSearchHistoryEntity)
}