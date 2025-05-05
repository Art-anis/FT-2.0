package com.example.search_flights

import com.example.network.api.FutureFlightsAPI
import com.example.network.models.ResponseFutureFlight

//репозиторий для поиска рейсов
class FlightsSearchRepository(
    private val api: FutureFlightsAPI //api рейсов
) {

    //поиск рейсов
    suspend fun searchFlights(departure: String, arrival: String, type: String, date: String): List<ResponseFutureFlight> {
        //работаем по-разному в зависимости от типа даты
        return if (type == "departure") {
            //если дата вылета, то сначала получаем все рейсы из аэропорта вылета в данную дату
            val allFlights = api.getFlights(airportIata = departure, destinationType = type, date = date)
            //затем фильтруем по аэропорту прибытия и сортируем по времени вылета
            allFlights.filter { it -> it.arrival?.iataCode == arrival.lowercase()}.sortedBy { it.departure?.scheduledTime }
        }
        //иначе делаем наоборот
        else {
            val allFlights = api.getFlights(airportIata = arrival, destinationType = type, date = date)
            allFlights.filter { it -> it.departure?.iataCode == departure.lowercase()}.sortedBy { it.departure?.scheduledTime }
        }
    }
}