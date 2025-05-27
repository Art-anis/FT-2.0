package com.example.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//класс аэропорта в БД
@Entity(tableName = "airports")
data class AirportEntity(
    @PrimaryKey
    @ColumnInfo(name = "iata_code") val iata: String, //код iata
    @ColumnInfo(name = "gmt") val gmt: String, //разница с Гринвичем
    @ColumnInfo(name = "airport_name") val name: String, //название
    @ColumnInfo(name = "icao_code") val icao: String, //код icao
    @ColumnInfo(name = "city_iata_code") val cityIata: String, //код iata города
    @ColumnInfo(name = "country_code") val countryCode: String, //код страны
    @ColumnInfo(name = "country_name") val countryName: String, //название страны
    @ColumnInfo(name = "last_search") val lastSearchTime: Long = 0 //дата последнего поиска
)