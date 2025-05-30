package com.example.ft.search.search_flights

import android.preference.PreferenceManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ft.App
import com.example.search_airports.AirportSearchRepository
import com.example.search_airports.util.AirportUIModel
import com.example.search_flights.FlightsSearchRepository
import com.example.search_flights.util.FlightSearchUIModel
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

//viewmodel для поиска рейсов
class FlightsSearchViewModel(
    private val flightsSearchRepository: FlightsSearchRepository,
    private val airportSearchRepository: AirportSearchRepository
): ViewModel() {

    //состояние фрагмента поиска
    private var _searchModel: MutableLiveData<FlightSearchUIModel> = MutableLiveData(FlightSearchUIModel())
    val searchModel: LiveData<FlightSearchUIModel>
        get() = _searchModel

    private var _searchHistory: MutableLiveData<List<String>> = MutableLiveData(emptyList())
    val searchHistory: LiveData<List<String>>
        get() = _searchHistory

    //при создании viewmodel инициализируем данные
    init {
        //при создании viewmodel инициализируем дату - через неделю после текущей даты
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, 8)
        _searchModel.value = _searchModel.value?.copy(date = calendar.time)
        getHistory()
    }

    //выбор вылета
    fun setDeparture(airport: AirportUIModel): Boolean {
        //аэропорт вылета и прибытия не должны совпадать
        if (_searchModel.value?.arrival == airport) {
            return false
        }
        //устанаваливаем аэропорт в UI-модели
        searchModel.value?.let {
            _searchModel.value!!.departure = airport
            val pref = PreferenceManager.getDefaultSharedPreferences(App.getInstance().applicationContext)
            val username = pref.getString("activeUser", "") ?: ""
            viewModelScope.launch {
                airportSearchRepository.addToHistory(username = username, iata = airport.iataCode)
            }
        }
        return true
    }

    //выбор прилета
    fun setArrival(airport: AirportUIModel): Boolean {
        //аэропорт вылета и прибытия не должны совпадать
        if (_searchModel.value?.departure == airport) {
            return false
        }
        _searchModel.value?.let {
            _searchModel.value!!.arrival = airport
            val pref = PreferenceManager.getDefaultSharedPreferences(App.getInstance().applicationContext)
            val username = pref.getString("activeUser", "") ?: ""
            viewModelScope.launch {
                airportSearchRepository.addToHistory(username = username, iata = airport.iataCode)
            }
        }
        return true
    }

    //обновление даты
    fun updateDate(date: Date) {
        _searchModel.value = _searchModel.value?.copy(date = date)
    }

    //сброс значения аэропорта вылета
    fun clearDeparture() {
        _searchModel.value = _searchModel.value?.copy(departure = AirportUIModel())
    }

    //сброс значения аэропорта прибытия
    fun clearArrival() {
        _searchModel.value = _searchModel.value?.copy(arrival = AirportUIModel())
    }


    fun getHistory() {
        viewModelScope.launch {
            val pref = PreferenceManager.getDefaultSharedPreferences(App.getInstance().applicationContext)
            val username = pref.getString("activeUser", "") ?: ""
            _searchHistory.value = flightsSearchRepository.getHistory(username)
        }
    }
}