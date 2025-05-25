package com.example.ft.search.search_airports

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ft.util.DestinationType
import com.example.search_airports.util.AirportUIModel
import org.koin.androidx.compose.koinViewModel

//экран поиска аэропортов
@Composable
fun AirportSearchScreen(
    airportType: DestinationType, //вариант назначения
    onNavigateBack: () -> Unit, //обратная навигация
    setAirport: (AirportUIModel) -> Boolean //функция выбора аэропорта в зависимости от типа
) {

    val viewModel = koinViewModel<AirportsSearchViewModel>()
    //список аэропортов в поиске
    val airports = viewModel.airportSearchState.observeAsState()

    //состояние значения поиска
    val query = rememberTextFieldState()

    //контейнер
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp)
    ) {
        //поисковая строка
        AirportSearchBar(
            query = query,
            searchAirports = viewModel::searchAirport,
            clearSearch = viewModel::clearSearch,
            onNavigateBack = onNavigateBack
        )
        //результаты и история поиска
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            //история поиска
            airports.value?.searchHistory?.let {
                airportSearchHistory(
                    history = it,
                    airportType = airportType,
                    onNavigateBack = onNavigateBack,
                    viewModel = viewModel,
                    setAirport = setAirport
                )
            }

            //результаты поиска
            airports.value?.searchResult?.let {
                airportSearchResults(
                    searchResult = it,
                    loading = airports.value?.loading == true,
                    query = query,
                    airportType = airportType,
                    onNavigateBack = onNavigateBack,
                    viewModel = viewModel,
                    setAirport = setAirport
                )
            }
        }
    }

    //при переходе назад очищаем результаты поиска
    BackHandler {
        viewModel.clearSearch()
        onNavigateBack()
    }
}