package com.example.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flight_search_history")
data class FlightSearchHistoryEntity(
    @PrimaryKey
    val searchTime: Long,
    val departure: String,
    val arrival: String,
    val departureDate: String
)
