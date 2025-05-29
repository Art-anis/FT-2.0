package com.example.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(tableName = "airport_search_history",
    primaryKeys = ["search_time", "username"],
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = arrayOf("username"),
        childColumns = arrayOf("username"),
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = arrayOf("airport_iata"), unique = true)]
)
data class AirportSearchHistoryEntity(
    @ColumnInfo("search_time") val searchTime: Long,
    @ColumnInfo("username") val username: String,
    @ColumnInfo("airport_iata") val airportIata: String
)
