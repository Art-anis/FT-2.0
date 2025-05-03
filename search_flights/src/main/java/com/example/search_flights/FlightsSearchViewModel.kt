package com.example.search_flights

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.search_airports.AirportSearchRepository
import com.example.search_airports.util.AirportSearchState
import com.example.search_airports.util.AirportUIModel
import com.example.search_flights.util.FlightSearchUIModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

//viewmodel для поиска рейсов
class FlightsSearchViewModel(
    private val airportSearchRepository: AirportSearchRepository,
    private val flightsSearchRepository: FlightsSearchRepository
): ViewModel() {

    //состояние фрагмента поиска
    private var _searchModel: MutableLiveData<FlightSearchUIModel> = MutableLiveData(FlightSearchUIModel())
    val searchModel: LiveData<FlightSearchUIModel>
        get() = _searchModel

    //состояние поиска аэропортов
    private var _airportSearchState: MutableLiveData<AirportSearchState> = MutableLiveData(AirportSearchState())
    val airportSearchState: LiveData<AirportSearchState>
        get() = _airportSearchState

    //ссылка на загрузочную корутину
    private var loadingJob: Job = Job()

    //при создании viewmodel инициализируем данные
    init {
        //при создании viewmodel инициализируем дату - через неделю
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, 8)
        _searchModel.value = _searchModel.value?.copy(date = calendar.time)

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
            val result = airportSearchRepository.searchAirports(query)
            //обновляем UI-состояние
            _airportSearchState.value = _airportSearchState.value?.copy(searchResult = result, loading = false)
        }
    }

    //очищаем поиск
    fun clearSearch() {
        _airportSearchState.value = _airportSearchState.value?.copy(searchResult = listOf())
    }

    //выбор вылета
    fun setDeparture(airport: AirportUIModel): Boolean {
        //аэропорт вылета и прибытия не должны совпадать
        if (_searchModel.value?.arrival == airport) {
            return false
        }
        //устанаваливаем аэропорт в UI-модели
        _searchModel.value = _searchModel.value?.copy(departure = airport)
        //обновляем дату для истории поиска
        selectAirport(airport.iataCode)
        return true
    }

    //выбор прилета
    fun setArrival(airport: AirportUIModel): Boolean {
        //аэропорт вылета и прибытия не должны совпадать
        if (_searchModel.value?.departure == airport) {
            return false
        }
        _searchModel.value = _searchModel.value?.copy(arrival = airport)
        selectAirport(airport.iataCode)
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

    //выбор аэропорта
    private fun selectAirport(iata: String) {
        //новая дата
        val newDate = Calendar.getInstance().timeInMillis
        viewModelScope.launch {
            //обновление даты
            airportSearchRepository.selectAirport(iata, newDate)
            refreshHistory()
        }
    }

    //обновление истории поиска
    private fun refreshHistory() {
        //подтягиваем историю
        viewModelScope.launch {
            val history = airportSearchRepository.getHistory()
            _airportSearchState.value = _airportSearchState.value?.copy(searchHistory = history)
        }
    }
}