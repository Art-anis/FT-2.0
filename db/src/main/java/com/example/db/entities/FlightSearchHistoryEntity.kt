package com.example.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "flight_search_history",
    indices = [Index(value = ["departure", "arrival", "departureDate", "username"], unique = true)],
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = arrayOf("username"),
        childColumns = arrayOf("username"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class FlightSearchHistoryEntity(
    @PrimaryKey
    val searchTime: Long,
    val username: String,
    val departure: String,
    val arrival: String,
    val departureDate: String
)
