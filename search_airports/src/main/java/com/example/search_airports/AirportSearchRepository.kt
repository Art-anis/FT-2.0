package com.example.search_airports

import com.example.db.dao.AirportDao
import com.example.db.dao.AirportSearchHistoryDao
import com.example.db.dao.CityDao
import com.example.db.entities.AirportSearchHistoryEntity
import com.example.network.api.NearbyAPI
import com.example.search_airports.util.AirportUIModel
import com.example.search_airports.util.toUIModel
import java.util.Date

//репозиторий для поиска аэропортов
class AirportSearchRepository(
    private val airportDao: AirportDao, //dao аэропортов
    private val airportSearchHistoryDao: AirportSearchHistoryDao,
    private val cityDao: CityDao, //dao городов
    private val nearbyApi: NearbyAPI //api для ближайших аэропортов
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

    suspend fun getNearbyAirports(lat: Double, long: Double): List<AirportUIModel> {
        val nearbyAirports = nearbyApi.getNearbyAirports(lat, long)
        return nearbyAirports.mapNotNull { airport ->
            val entity = airportDao.getAirportByIata(airport.codeIataAirport ?: "")
            entity?.let {
                val city = cityDao.getCityByIata(entity.cityIata)
                //если есть, добавляем в результирующий список
                city?.let {
                    entity.toUIModel(cityName = city.name)
                }
            }
        }
    }

    suspend fun addToHistory(username: String, iata: String) {
        airportSearchHistoryDao.addToHistory(AirportSearchHistoryEntity(
            searchTime = Date().time,
            username = username,
            airportIata = iata
        ))
    }

    //получение истории поиска (последних 2 значений)
    suspend fun getHistory(username: String): List<AirportUIModel> {
        val history = airportSearchHistoryDao.getHistory(username)
        return history.mapNotNull { airportHistory ->
            val airport = airportDao.getAirportByIata(airportHistory.airportIata)
            airport?.let {
                val city = cityDao.getCityByIata(airport.cityIata)
                city?.let {
                    airport.toUIModel(cityName = city.name)
                }
            }
        }
    }

    //выбор аэропорта в поиске (обновляется дата)
    suspend fun updateDate(iata: String, date: Long) {
        airportDao.updateDate(iata, date)
    }
}