package com.example.search_airports

import com.example.db.dao.AirportDao
import com.example.db.dao.CityDao
import com.example.search_airports.util.AirportUIModel
import com.example.search_airports.util.toUIModel

//репозиторий для поиска аэропортов
class AirportSearchRepository(
    private val airportDao: AirportDao, //dao аэропортов
    private val cityDao: CityDao //dao городов
) {
    //поиск аэропортов по тексту
    suspend fun searchAirports(query: String): List<AirportUIModel> {
        //получение всех аэропортов, соответствующих запросу
        val airports = airportDao.searchAirports(query = "$query%")
        //маппинг результатов
        return airports.mapNotNull { airport ->
            //ищем соответствующий город
            val city = cityDao.getCityByIata(airport.cityIata)
            //если есть, добавляем в результирующий список
            city?.let {
                airport.toUIModel(cityName = city.name)
            }
        }
    }

    //получение истории поиска (последних 3 значений)
    suspend fun getHistory(): List<AirportUIModel> {
        val history = airportDao.getHistory()
        return history.mapNotNull { airport ->
            val city = cityDao.getCityByIata(airport.cityIata)
            city?.let {
                airport.toUIModel(cityName = city.name)
            }
        }
    }

    //выбор аэропорта в поиске (обновляется дата)
    suspend fun updateDate(iata: String, date: Long) {
        airportDao.updateDate(iata, date)
    }
}