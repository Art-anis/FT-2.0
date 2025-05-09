package com.example.airport

import com.example.db.dao.AirportDao

//репозиторий аэропортов
class AirportRepository(
    private val dao: AirportDao //dao
) {

    //получение часового пояса аэропорта
    suspend fun getAirportGmt(iata: String): String? {
        return dao.getAirportByIata(iata)?.gmt
    }
}