package com.example.search_flights

import com.example.db.dao.FlightSearchHistoryDao
import com.example.db.entities.FlightSearchHistoryEntity
import com.example.network.api.FutureFlightsAPI
import com.example.network.models.ResponseFutureFlight
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//репозиторий для поиска рейсов
class FlightsSearchRepository(
    private val api: FutureFlightsAPI, //api рейсов
    private val historyDao: FlightSearchHistoryDao
) {

    //список ответов из api
    private var _searchResult: List<ResponseFutureFlight> = listOf()
    val searchResult: List<ResponseFutureFlight>
        get() = _searchResult

    //поиск рейсов
    suspend fun searchFlights(departureIata: String, departureCity: String, arrivalIata: String, arrivalCity: String, date: Long, username: String) {
        //форматируем дату
        val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(Date(date))

        //сначала получаем все рейсы из аэропорта вылета в данную дату
        val allFlights = api.getFlights(airportIata = departureIata, destinationType = "departure", date = formattedDate)
        if (allFlights.isSuccessful) {
            val body = allFlights.body()
            body?.let { result ->
                //затем фильтруем по аэропорту прибытия и сортируем по времени вылета
                val filteredFlights = result
                    .filter { it.arrival?.iataCode == arrivalIata.lowercase() }
                    .sortedBy { it.departure?.scheduledTime }

                //группируем рейсы по вылету и прибытию, чтобы потом исключить дубликаты
                val groupedFlights = filteredFlights.groupBy { flight -> Pair(flight.departure, flight.arrival) }
                _searchResult = groupedFlights.map { entry ->
                    //если единственный в группе, то возвращаем его
                    if (entry.value.size == 1) {
                        entry.value[0]
                    }
                    //иначе выбираем единственного с кодшерингом, потому что остальные - это те же рейсы для других авиалиний
                    else {
                        entry.value.filter { it.codeshared != null }[0]
                    }
                }
            }

            historyDao.addFlightToHistory(FlightSearchHistoryEntity(
                searchTime = Date().time,
                departure = "$departureCity, $departureIata",
                arrival = "$arrivalCity, $arrivalIata",
                departureDate = formattedDate,
                username = username
            ))
        }
    }

    //выбор рейса из результатов поиска
    fun findFlight(flightNumber: String): ResponseFutureFlight? {
        return _searchResult.find { "${it.airline?.iataCode} ${it.flight?.number}".uppercase() == flightNumber }
    }

    suspend fun getHistory(username: String): List<String> {
        val result = historyDao.getLastSearchedFlights(username)
        return result.map { "From ${it.departure} to ${it.arrival} at ${it.departureDate}" }
    }
}