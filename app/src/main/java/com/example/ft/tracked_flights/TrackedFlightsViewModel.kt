package com.example.ft.tracked_flights

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tracked_flights.TrackedFlightsRepository
import com.example.tracked_flights.util.TrackedFlightUIModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TrackedFlightsViewModel(
    private val repository: TrackedFlightsRepository
): ViewModel() {

    private var _trackedFlightList: MutableLiveData<List<TrackedFlightUIModel>> =
        MutableLiveData(emptyList())
    val trackedFlightList: LiveData<List<TrackedFlightUIModel>>
        get() = _trackedFlightList

    private var _loading: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: LiveData<Boolean>
        get() = _loading

    fun getFlightList() {
        viewModelScope.launch {
            _loading.value = true
            _trackedFlightList.value = repository.getAllTrackedFlights()
            _loading.value = false
        }
    }

    suspend fun getTrackedFlight(iata: String, date: Long): TrackedFlightUIModel? {
        return viewModelScope.async {
            repository.getTrackedFlight(iata, date)
        }.await()
    }
}