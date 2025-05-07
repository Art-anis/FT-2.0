package com.example.view_flight

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.search_flights.FlightsSearchRepository
import com.example.view_flight.util.ViewFlightUIModel
import com.example.view_flight.util.toUIModel

//viewmodel для экрана просмотра рейса
class ViewFlightViewModel(
    private val flightsSearchRepository: FlightsSearchRepository //репозиторий
): ViewModel() {

    //данные о рейсе
    private var _flightData: MutableLiveData<ViewFlightUIModel> = MutableLiveData()
    val flightData: LiveData<ViewFlightUIModel>
        get() = _flightData

    //загрузка аэропорта из репозитория
    fun loadAirport(flightNumber: String, departure: String, arrival: String) {
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
}