package com.example.search_airports

import com.example.db.dao.AirportDao
import com.example.db.dao.CityDao
import com.example.network.api.NearbyAPI
import com.example.search_airports.util.AirportUIModel
import com.example.search_airports.util.toUIModel

//репозиторий для поиска аэропортов
class AirportSearchRepository(
    private val airportDao: AirportDao, //dao аэропортов
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