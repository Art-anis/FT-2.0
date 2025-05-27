package com.example.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.db.entities.FlightSearchHistoryEntity

@Dao
interface FlightSearchHistoryDao {

    @Query("select * from flight_search_history where username = :username order by searchTime desc limit 5")
    suspend fun getLastSearchedFlights(username: String): List<FlightSearchHistoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFlightToHistory(flight: FlightSearchHistoryEntity)
}