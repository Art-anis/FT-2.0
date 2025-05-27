package com.example.db.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "flight_search_history", indices = [Index(value = ["departure", "arrival", "departureDate"], unique = true)])
data class FlightSearchHistoryEntity(
    @PrimaryKey
    val searchTime: Long,
    val departure: String,
    val arrival: String,
    val departureDate: String
)
