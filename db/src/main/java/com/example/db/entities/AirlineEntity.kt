package com.example.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "airlines")
data class AirlineEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id") val id: Int = 0,
    @ColumnInfo("iata_code") val iataCode: String,
    @ColumnInfo("icao_code") val icaoCode: String,
    @ColumnInfo("name") val name: String
)
