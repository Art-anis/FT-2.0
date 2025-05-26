package com.example.airport

import com.example.db.dao.AirportDao
import com.example.db.dao.CityDao
import com.example.network.api.FutureFlightsAPI
import com.example.network.models.ResponseFutureFlight

//репозиторий аэропортов
class AirportRepository(
    private val airportDao: AirportDao, //dao
    private val cityDao: CityDao,
    private val api: FutureFlightsAPI
) {

    //список ответов из api
    private var _searchResult: List<Pair<ResponseFutureFlight, String>> = listOf()
    val searchResult: List<Pair<ResponseFutureFlight, String>>
        get() = _searchResult

    //получение часового пояса аэропорта
    suspend fun getAirportGmt(iata: String): String? {
        return airportDao.getAirportByIata(iata)?.gmt
    }

    suspend fun getTimetable(iata: String, date: String) {
        val result = api.getFlights(
            airportIata = iata,
            destinationType = "departure",
            date = date
        )
        if (result.code() == 200) {
            result.body()?.let {
                //группируем рейсы по вылету и прибытию, чтобы потом исключить дубликаты
                val groupedFlights = it.groupBy { flight -> Pair(flight.departure, flight.arrival) }
                _searchResult = groupedFlights.mapNotNull { entry ->
                    //если единственный в группе, то возвращаем его
                    if (entry.value.size == 1) {
                        val arrivalAirport = airportDao.getAirportByIata(entry.value[0].arrival?.iataCode?.uppercase() ?: "")
                        arrivalAirport?.let {
                            val arrivalCity = cityDao.getCityByIata(arrivalAirport.cityIata)
                            arrivalCity?.let {
                                entry.value[0] to arrivalCity.name
                            }
                        }
                    }
                    //иначе выбираем единственного с кодшерингом, потому что остальные - это те же рейсы для других авиалиний
                    else {
                        val filteredFlight = entry.value.filter { it.codeshared != null }[0]
                        val arrivalAirport = airportDao.getAirportByIata(filteredFlight.arrival?.iataCode?.uppercase() ?: "")
                        arrivalAirport?.let {
                            val arrivalCity = cityDao.getCityByIata(arrivalAirport.cityIata)
                            arrivalCity?.let {
                                filteredFlight to arrivalCity.name
                            }
                        }
                    }
                }
            } ?: {
                _searchResult = emptyList()
            }
        }
        else {
            _searchResult = emptyList()
        }
    }

    fun findFlight(flightNumber: String): ResponseFutureFlight? {
        return _searchResult.find { "${it.first.airline?.iataCode} ${it.first.flight?.number}".uppercase() == flightNumber }?.first
    }
}