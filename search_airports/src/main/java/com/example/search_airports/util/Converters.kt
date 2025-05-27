package com.example.search_airports.util

import com.example.db.entities.AirportEntity

//конвертер модели аэропорта в БД в UI-модель
fun AirportEntity.toUIModel(cityName: String): AirportUIModel {
    return AirportUIModel(
        airportName = this.name,
        cityName = cityName,
        countryName = this.countryName,
        countryCode = this.countryCode,
        iataCode = this.iata
    )
}