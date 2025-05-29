package com.example.ft.search.search_airports

import android.Manifest
import android.content.Context.LOCATION_SERVICE
import android.location.LocationManager
import android.preference.PreferenceManager
import androidx.annotation.RequiresPermission
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ft.App
import com.example.search_airports.AirportSearchRepository
import com.example.search_airports.util.AirportSearchState
import com.example.search_airports.util.AirportUIModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.Calendar

//viewmodel поиска аэропортов
class AirportsSearchViewModel(
    private val repository: AirportSearchRepository //репозиторий
): ViewModel() {

    //состояние поиска аэропортов
    private var _airportSearchState: MutableLiveData<AirportSearchState> = MutableLiveData(AirportSearchState())
    val airportSearchState: LiveData<AirportSearchState>
        get() = _airportSearchState

    private var _nearbyAirports: MutableLiveData<List<AirportUIModel>> = MutableLiveData(emptyList())
    val nearbyAirports: LiveData<List<AirportUIModel>>
        get() = _nearbyAirports

    private var _nearbyLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val nearbyLoading: LiveData<Boolean>
        get() = _nearbyLoading

    //ссылка на загрузочную корутину
    private var loadingJob: Job = Job()

    init {
        refreshHistory()
    }

    //поиск аэропорта
    fun searchAirport(query: String) {
        //если до этого был запущен поиск, то отменяем его
        if (_airportSearchState.value?.loading == true) {
            loadingJob.cancel()
            _airportSearchState.value?.loading = false
        }
        //запускаем новый поиск
        loadingJob = viewModelScope.launch {
            //ставим триггер загрузки
            _airportSearchState.value?.loading = true
            //ищем аэропорты в БД
            val result = repository.searchAirports(query)
            //обновляем UI-состояние
            _airportSearchState.value = _airportSearchState.value?.copy(searchResult = result, loading = false)
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun getNearbyAirports() {
        val locationManager = App.getInstance().getSystemService(LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER, 15000, 500f
        ) { location ->
            val lat = location.latitude
            val lng = location.longitude

            viewModelScope.launch {
                _nearbyLoading.value = true
                _nearbyAirports.value = repository.getNearbyAirports(lat, lng)
                _nearbyLoading.value = false
            }
        }
    }

    //очищаем поиск
    fun clearSearch() {
        if (_airportSearchState.value?.loading == true) {
            loadingJob.cancel()
            _airportSearchState.value?.loading = false
        }
        _airportSearchState.value = _airportSearchState.value?.copy(searchResult = listOf())
    }

    //обновление истории поиска
    private fun refreshHistory() {
        //подтягиваем историю
        viewModelScope.launch {
            val pref = PreferenceManager.getDefaultSharedPreferences(App.getInstance().applicationContext)
            val username = pref.getString("activeUser", "") ?: ""
            val history = repository.getHistory(username)
            _airportSearchState.value = _airportSearchState.value?.copy(searchHistory = history)
        }
    }

    //выбор аэропорта
    fun updateDate(iata: String) {
        //новая дата
        val newDate = Calendar.getInstance().timeInMillis
        viewModelScope.launch {
            //обновление даты
            repository.updateDate(iata, newDate)
            refreshHistory()
        }
    }
}