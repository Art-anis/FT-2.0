package com.example.flight_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flight_list.util.FlightItemUIModel
import com.example.flight_list.util.toItemUIModel
import com.example.search_flights.FlightsSearchRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
    fun searchFlights(departure: String, arrival: String, type: String, date: Long) {
        viewModelScope.launch {
            //форматируем дату
            val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(date))

            //обновляем состояние загрузки
            _loading.value = true
            //поиск
            flightSearchRepository.searchFlights(
                departure = departure,
                arrival = arrival,
                type = type,
                date = formattedDate
            )
            _flightList.value = flightSearchRepository.searchResult.map { it.toItemUIModel() }
            //обновляем состояние загрузки
            _loading.value = false
        }
    }
}