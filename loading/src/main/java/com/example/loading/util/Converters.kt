package com.example.loading.util

import com.example.db.entities.AirportEntity
import com.example.network.models.ResponseAirport

//конвертируем ответ API в модель БД
fun ResponseAirport.toEntity(): AirportEntity {
    return AirportEntity(
        gmt = this.gmt ?: "0",
        name = this.nameAirport!!,
        icao = this.codeIcaoAirport ?: "",
        iata = this.codeIataAirport!!,
        cityIata = this.codeIataCity ?: "",
        countryName = this.nameCountry ?: ""
    )
}