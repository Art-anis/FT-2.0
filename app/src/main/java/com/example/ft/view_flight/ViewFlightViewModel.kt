package com.example.ft.view_flight

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airport.AirportRepository
import com.example.ft.util.toViewFlightUIModel
import com.example.search_flights.FlightsSearchRepository
import com.example.tracked_flights.TrackedFlightsRepository
import com.example.tracked_flights.util.AirlineData
import com.example.tracked_flights.util.DestinationData
import com.example.view_flight.util.ViewFlightUIModel
import com.example.view_flight.util.toUIModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.Calendar

//viewmodel для экрана просмотра рейса
class ViewFlightViewModel(
    private val flightsSearchRepository: FlightsSearchRepository, //репозиторий поиска рейсов
    private val airportRepository: AirportRepository, //репозиторий аэропортов
    private val trackedFlightsRepository: TrackedFlightsRepository //репозиторий для отслеживания рейсов
): ViewModel() {

    //данные о рейсе
    private var _flightData: MutableLiveData<ViewFlightUIModel> = MutableLiveData()
    val flightData: LiveData<ViewFlightUIModel>
        get() = _flightData

    //загрузка аэропорта из репозитория
    fun getFlight(flightNumber: String, departure: String, arrival: String) {
        //ищем рейс в репозитории
        val result = flightsSearchRepository.findFlight(flightNumber)
        //если нашли, то сохраняем во viewmodel
        result?.let {
            _flightData.value = it.toUIModel(
                departureCityName = departure,
                arrivalCityName = arrival
            )
        }
    }

    //получение отслеживаемого рейса
    fun getTrackedFlight(iata: String, date: Long) {
        viewModelScope.launch {
            val result = trackedFlightsRepository.getTrackedFlight(iata, date)
            result?.let {
                _flightData.value = it.toViewFlightUIModel()
            }
        }
    }

    //отслеживаем рейс
    suspend fun trackFlight(flight: ViewFlightUIModel, date: Long): Int {
        //разбиваем часы и минуты
        val (arrivalHours, arrivalMinutes) = flight.arrival.time.split(":").map { it.toInt() }
        //устанавливаем их в календарь
        val arrivalCalendar = Calendar.getInstance()
        arrivalCalendar.set(Calendar.HOUR_OF_DAY, arrivalHours)
        arrivalCalendar.set(Calendar.MINUTE, arrivalMinutes)

        //собираем данные о вылете
        val departure = DestinationData(
            airportIata = flight.departure.iata,
            cityName = flight.departure.cityName,
            terminal = flight.departure.terminal,
            gate = flight.departure.gate,
            time = date
        )

        //собираем данные о прибытии
        val arrival = DestinationData(
            airportIata = flight.arrival.iata,
            cityName = flight.arrival.cityName,
            terminal = flight.arrival.terminal,
            gate = flight.arrival.gate,
            time = arrivalCalendar.timeInMillis
        )

        //собираем данные об авиалинии
        val airline = AirlineData(
            airlineIata = flight.airline.airlineIata,
            flightNumber = flight.airline.flightNumber,
            airlineName = flight.airline.airlineName
        )

        //собираем данные о кодшеринге, если он есть
        val codeshared = flight.codeshared?.let {
            AirlineData(
                airlineIata = it.airlineIata,
                flightNumber = it.flightNumber,
                airlineName = it.airlineName
            )
        }

        //запуск отслеживания рейса
        return viewModelScope.async {
            trackedFlightsRepository.trackFlight(
                departure = departure,
                arrival = arrival,
                mainAirline = airline,
                codeshared = codeshared
            )
        }.await()
    }

    //проверка, отслеживаемый ли рейс
    suspend fun checkIfTracked(flightIata: String, date: Long): Boolean {
        val result = viewModelScope.async {
            trackedFlightsRepository.getTrackedFlight(flightIata, date) != null
        }.await()
        return result
    }

    //получение часового пояса аэропорта
    suspend fun getGmt(iata: String): String? {
        val gmt = viewModelScope.async {
            airportRepository.getAirportGmt(iata.uppercase())
        }.await()
        return gmt
    }


}