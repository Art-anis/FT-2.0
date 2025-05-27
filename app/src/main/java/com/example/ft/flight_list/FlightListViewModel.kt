package com.example.ft.flight_list

import android.preference.PreferenceManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flight_list.util.FlightItemUIModel
import com.example.flight_list.util.toItemUIModel
import com.example.ft.App
import com.example.search_airports.util.AirportUIModel
import com.example.search_flights.FlightsSearchRepository
import kotlinx.coroutines.launch

//viewmodel для списка рейсов
class FlightListViewModel(
    private val flightSearchRepository: FlightsSearchRepository //репозиторий поиска рейсов
): ViewModel() {

    //результат поиска
    private val _flightList: MutableLiveData<List<FlightItemUIModel>> = MutableLiveData(emptyList())
    val flightList: LiveData<List<FlightItemUIModel>>
        get() = _flightList

    //состояние загрузки
    private val _loading: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: LiveData<Boolean>
        get() = _loading


    //процедура поиска рейсов
    fun searchFlights(departure: AirportUIModel, arrival: AirportUIModel, date: Long) {
        viewModelScope.launch {
            val pref = PreferenceManager.getDefaultSharedPreferences(App.getInstance().applicationContext)
            val username = pref.getString("activeUser", "") ?: ""
            //обновляем состояние загрузки
            _loading.value = true
            //поиск
            flightSearchRepository.searchFlights(
                departureIata = departure.iataCode,
                arrivalIata = arrival.iataCode,
                date = date,
                departureCity = departure.cityName,
                arrivalCity = arrival.cityName,
                username = username
            )
            _flightList.value = flightSearchRepository.searchResult.map { it.toItemUIModel() }
            //обновляем состояние загрузки
            _loading.value = false
        }
    }
}