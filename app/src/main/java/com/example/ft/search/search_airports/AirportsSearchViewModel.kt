package com.example.ft.search.search_airports

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.search_airports.AirportSearchRepository
import com.example.search_airports.util.AirportSearchState
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

    //очищаем поиск
    fun clearSearch() {
        _airportSearchState.value = _airportSearchState.value?.copy(searchResult = listOf())
    }

    //обновление истории поиска
    private fun refreshHistory() {
        //подтягиваем историю
        viewModelScope.launch {
            val history = repository.getHistory()
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