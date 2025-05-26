package com.example.ft.tracked_flights

import android.preference.PreferenceManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ft.App
import com.example.tracked_flights.TrackedFlightsRepository
import com.example.tracked_flights.util.TrackedFlightUIModel
import kotlinx.coroutines.launch

//viewmodel для отслеживаемых рейсов
class TrackedFlightsViewModel(
    private val repository: TrackedFlightsRepository
): ViewModel() {

    //список рейсов
    private var _trackedFlightList: MutableLiveData<List<TrackedFlightUIModel>> =
        MutableLiveData(emptyList())
    val trackedFlightList: LiveData<List<TrackedFlightUIModel>>
        get() = _trackedFlightList

    //флаг прогресса загрузки
    private var _loading: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: LiveData<Boolean>
        get() = _loading

    //получение списка
    fun getFlightList() {
        viewModelScope.launch {
            //получаем имя пользователя
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(App.getInstance().applicationContext)
            val username = sharedPref.getString("activeUser", "")
            //если все хорошо, добавляем рейс
            if (!username.isNullOrEmpty()) {
                _loading.value = true
                _trackedFlightList.value = repository.getAllTrackedFlights(username)
                _loading.value = false
            }
        }
    }

    //удаление рейса
    fun onDelete(iata: String, date: Long) {
        viewModelScope.launch {
            //получаем имя пользователя
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(App.getInstance().applicationContext)
            val username = sharedPref.getString("activeUser", "") ?: ""
            //удаляем рейс для этого пользователя
            repository.deleteTrackedFlight(iata, date, username)
        }
    }
}