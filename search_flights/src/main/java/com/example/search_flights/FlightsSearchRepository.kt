package com.example.search_flights

import com.example.network.api.FutureFlightsAPI
import com.example.network.models.ResponseFutureFlight

//репозиторий для поиска рейсов
class FlightsSearchRepository(
    private val api: FutureFlightsAPI //api рейсов
) {

    //список ответов из api
    private var _searchResult: List<ResponseFutureFlight> = listOf()
    val searchResult: List<ResponseFutureFlight>
        get() = _searchResult

    //поиск рейсов
    suspend fun searchFlights(departure: String, arrival: String, type: String, date: String) {
        //работаем по-разному в зависимости от типа даты
        if (type == "departure") {
            //если дата вылета, то сначала получаем все рейсы из аэропорта вылета в данную дату
            val allFlights = api.getFlights(airportIata = departure, destinationType = type, date = date)
            //затем фильтруем по аэропорту прибытия и сортируем по времени вылета
            _searchResult = allFlights.filter { it -> it.arrival?.iataCode == arrival.lowercase()}.sortedBy { it.departure?.scheduledTime }
        }
        //иначе делаем наоборот
        else {
            val allFlights = api.getFlights(airportIata = arrival, destinationType = type, date = date)
            _searchResult = allFlights.filter { it -> it.departure?.iataCode == departure.lowercase()}.sortedBy { it.departure?.scheduledTime }
        }
    }

    //выбор рейса из результатов поиска
    fun findFlight(flightNumber: String): ResponseFutureFlight? {
        return _searchResult.find { "${it.airline?.iataCode} ${it.flight?.number}".uppercase() == flightNumber }
    }
}