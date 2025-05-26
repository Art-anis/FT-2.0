package com.example.tracked_flights

import com.example.db.dao.AirlineDao
import com.example.db.dao.AirportDao
import com.example.db.dao.CityDao
import com.example.db.dao.TrackedFlightDao
import com.example.db.entities.TrackedFlightEntity
import com.example.tracked_flights.util.AirlineData
import com.example.tracked_flights.util.DestinationData
import com.example.tracked_flights.util.TrackedFlightUIModel
import com.example.tracked_flights.util.toUIModel
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.typeOf

class TrackedFlightsRepository(
    private val trackedFlightDao: TrackedFlightDao,
    private val airportDao: AirportDao,
    private val cityDao: CityDao,
    private val airlineDao: AirlineDao
) {

    //получение всех отслеживаемых рейсов
    suspend fun getAllTrackedFlights(username: String): List<TrackedFlightUIModel> {
        val result = trackedFlightDao.getAllFlights(username)
        return result.mapNotNull {
            //получение названий городов
            val departureCity = getCityName(it.departureIata.uppercase())
            val arrivalCity = getCityName(it.arrivalIata.uppercase())

            //получение авиалиний
            val airlineName = getAirlineName(it.airlineIata.uppercase())
            val codesharedName = getAirlineName(it.codesharedAirlineIata.uppercase())

            //конвертируем
            if (departureCity != null && arrivalCity != null && airlineName != null) {
                it.toUIModel(
                    departureCity = departureCity,
                    arrivalCity = arrivalCity,
                    airlineName = airlineName,
                    codesharedName = codesharedName
                )
            }
            else {
                null
            }
        }
    }

    //добавление рейса в отслеживаемые
    suspend fun trackFlight(
        departure: DestinationData,
        arrival: DestinationData,
        mainAirline: AirlineData,
        codeshared: AirlineData?,
        username: String
    ): Int {
        //собираем сущность
        val entity = TrackedFlightEntity(
            username = username,
            airlineIata = mainAirline.airlineIata.uppercase(),
            arrivalGate = arrival.gate,
            arrivalIata = arrival.airportIata.uppercase(),
            arrivalTerminal = arrival.terminal,
            scheduledDeparture = departure.time,
            estimatedDeparture = departure.time,
            codesharedAirlineIata = codeshared?.airlineIata?.uppercase() ?: "",
            codesharedFlightIata = codeshared?.flightNumber ?: "",
            departureGate = departure.gate,
            departureIata = departure.airportIata.uppercase(),
            departureTerminal = departure.terminal,
            scheduledArrival = arrival.time,
            estimatedArrival = arrival.time,
            flightIata = mainAirline.flightNumber,
            status = "scheduled"
        )

        trackedFlightDao.trackFlight(entity)
        //возвращаем id
        val result = trackedFlightDao.getFlightByIata(mainAirline.flightNumber, departure.time).find { it.username == username }
        result?.let {
            return it.id
        } ?: return -1
    }

    //обновление рейса
    suspend fun updateFlight(fields: Map<KProperty1<TrackedFlightEntity, *>, String>, flightNumber: String, date: Long, username: String) {
        //старая запись
        val oldFlight = trackedFlightDao.getFlightByIata(flightNumber, date).find { it.username == username }

        //конструктор
        val constructor = TrackedFlightEntity::class.primaryConstructor ?: return

        //параметры конструктора
        val params = constructor.parameters.associateBy { it.name }

        //аргументы
        val args = params.map { (name, parameter) ->
            //ищем поле в измененных
            val field = fields.keys.find { it.name == name }
            //если поле есть
            if (field != null) {
                //собираем поле
                when (field.returnType) {
                    typeOf<Long>() -> {
                        parameter to fields[field]?.toLong()
                    }
                    typeOf<Int>() -> {
                        parameter to fields[field]?.toInt()
                    }
                    else -> {
                        parameter to fields[field]
                    }
                }
            }
            //иначе просто получаем поле из старой записи
            else {
                val dbField = TrackedFlightEntity::class.memberProperties.first { it.name == name }
                parameter to dbField.getter.call(oldFlight)
            }
        }.toMap()
        //получаем новую запись и обновляем БД
        val newFlight = constructor.callBy(args)
        trackedFlightDao.updateFlight(newFlight)
    }

    //получение рейса
    suspend fun getTrackedFlight(iata: String, date: Long, username: String): TrackedFlightUIModel? {
        //получаем сам рейс
        val result = trackedFlightDao.getFlightByIata(iata, date)

        //выбираем рейс для текущего пользователя
        val currentFlight = result.find {
            it.username == username
        }

        currentFlight?.let {
            //получаем данные о городах
            val departureCity = getCityName(it.departureIata.uppercase())
            val arrivalCity = getCityName(it.arrivalIata.uppercase())

            //получение данных об авиалиний
            val airlineName = getAirlineName(it.airlineIata.uppercase())
            val codesharedName = getAirlineName(it.codesharedAirlineIata.uppercase())

            //конвертирование
            if (departureCity != null && arrivalCity != null && airlineName != null) {
                return it.toUIModel(
                    departureCity = departureCity,
                    arrivalCity = arrivalCity,
                    airlineName = airlineName,
                    codesharedName = codesharedName
                )
            }
            else {
                return null
            }
        } ?: return null
    }

    //удаление рейса
    suspend fun deleteTrackedFlight(iata: String, date: Long, username: String) {
        trackedFlightDao.deleteTrackedFlight(iata, date, username)
    }

    //получение названия авиалинии
    private suspend fun getAirlineName(airlineIata: String): String? {
        val airline = airlineDao.getAirlineByIata(airlineIata)
        return airline?.name
    }

    //получение название города
    private suspend fun getCityName(airportIata: String): String? {
        val airport = airportDao.getAirportByIata(airportIata)
        airport?.let {
            return cityDao.getCityByIata(it.cityIata)?.name
        } ?: return null
    }
}