package com.example.loading.util

import com.example.db.entities.AirlineEntity
import com.example.db.entities.AirportEntity
import com.example.network.models.ResponseAirline
import com.example.network.models.ResponseAirport

//конвертируем ответ API в модель БД
fun ResponseAirport.toEntity(): AirportEntity {
    return AirportEntity(
        gmt = this.gmt ?: "0",
        name = this.nameAirport!!,
        icao = this.codeIcaoAirport ?: "",
        iata = this.codeIataAirport!!,
        cityIata = this.codeIataCity ?: "",
        countryCode = this.codeIso2Country ?: "",
        countryName = this.nameCountry ?: ""
    )
}

fun ResponseAirline.toEntity(): AirlineEntity {
    return AirlineEntity(
        iataCode = this.codeIataAirline ?: "",
        icaoCode = this.codeIcaoAirline ?: "",
        name = this.nameAirline ?: ""
    )
}