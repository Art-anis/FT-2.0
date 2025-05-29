package com.example.ft.airport_timetable

import android.preference.PreferenceManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airport.AirportRepository
import com.example.flight_list.util.FlightItemUIModel
import com.example.flight_list.util.toItemUIModel
import com.example.ft.App
import com.example.search_airports.AirportSearchRepository
import com.example.search_airports.util.AirportUIModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AirportTimetableViewModel(
    private val airportRepository: AirportRepository,
    private val airportSearchRepository: AirportSearchRepository
): ViewModel() {

    private var _selectedAirport: MutableLiveData<AirportUIModel> = MutableLiveData(AirportUIModel())
    val selectedAirport: LiveData<AirportUIModel>
        get() = _selectedAirport

    private var _timetable: MutableLiveData<List<Pair<FlightItemUIModel, String>>> = MutableLiveData()
    val timetable: LiveData<List<Pair<FlightItemUIModel, String>>>
        get() = _timetable

    private var _loadingTimetable: MutableLiveData<Boolean> = MutableLiveData(false)
    val loadingTimetable: LiveData<Boolean>
        get() = _loadingTimetable

    fun setAirport(airport: AirportUIModel): Boolean {
        _selectedAirport.value = airport
        val pref = PreferenceManager.getDefaultSharedPreferences(App.getInstance().applicationContext)
        val username = pref.getString("activeUser", "") ?: ""
        viewModelScope.launch {
            airportSearchRepository.addToHistory(username = username, iata = airport.iataCode)
        }
        return true
    }

    fun clearSearch() {
        _selectedAirport.value = AirportUIModel()
    }

    fun getTimetable(iata: String, date: Long) {
        //форматируем дату
        val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(Date(date))

        viewModelScope.launch {
            _loadingTimetable.value = true
            airportRepository.getTimetable(iata, formattedDate)
            _timetable.value = airportRepository.searchResult.map { it.first.toItemUIModel() to it.second }
            _loadingTimetable.value = false
        }
    }
}