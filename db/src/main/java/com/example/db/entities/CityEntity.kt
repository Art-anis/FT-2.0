package com.example.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//таблица городов
@Entity(tableName = "cities")
data class CityEntity(
    @PrimaryKey
    @ColumnInfo(name = "iata_code") val iata: String, //код iata
    @ColumnInfo(name = "city_name") val name: String, //название
    @ColumnInfo("time_zone") val timeZone: String //часовой пояс
)