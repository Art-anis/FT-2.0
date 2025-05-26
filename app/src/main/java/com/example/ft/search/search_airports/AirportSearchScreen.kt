package com.example.ft.search.search_airports

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.ft.R
import com.example.ft.util.DestinationType
import com.example.search_airports.util.AirportUIModel
import org.koin.androidx.compose.koinViewModel

//экран поиска аэропортов
@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AirportSearchScreen(
    airportType: DestinationType?, //вариант назначения
    onNavigateBack: () -> Unit, //обратная навигация
    setAirport: (AirportUIModel) -> Boolean //функция выбора аэропорта в зависимости от типа
) {

    val viewModel = koinViewModel<AirportsSearchViewModel>()
    //список аэропортов в поиске
    val airports = viewModel.airportSearchState.observeAsState()

    val nearbyAirports = viewModel.nearbyAirports.observeAsState()
    val nearbyLoading = viewModel.nearbyLoading.observeAsState()

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

            item {
                var expanded by rememberSaveable { mutableStateOf(false) }

                var value by rememberSaveable { mutableStateOf("Select nearby airport") }
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    }
                ) {
                    TextField(
                        modifier = Modifier.menuAnchor(
                            type = MenuAnchorType.PrimaryNotEditable,
                            enabled = true
                        ),
                        value = value,
                        onValueChange = {
                            value = it
                        },
                        readOnly = true
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        }
                    ) {
                        if (!nearbyAirports.value.isNullOrEmpty()) {
                            val context = LocalContext.current
                            nearbyAirports.value!!.forEach { airport ->
                                AirportItem(
                                    airport = airport,
                                    onSelected = {
                                        //если установка завершилась неудачно (т.е. в другом поле уже выбран этот аэропорт
                                        if (!setAirport(airport)) {
                                            //выводим соответствующий тост
                                            when(airportType) {
                                                DestinationType.DEPARTURE -> Toast.makeText(context,
                                                    context.getString(R.string.departure_repeat), Toast.LENGTH_SHORT).show()
                                                DestinationType.ARRIVAL -> Toast.makeText(context,
                                                    context.getString(R.string.arrival_repeat), Toast.LENGTH_SHORT).show()
                                                else -> {}
                                            }
                                        }
                                        //иначе выбор прошел успешно
                                        else {
                                            //обновляем дату выбора аэропорта
                                            viewModel.updateDate(airport.iataCode)
                                            //очищаем поиск
                                            viewModel.clearSearch()
                                            //возвращаемся назад
                                            onNavigateBack()
                                        }
                                    }
                                )
                            }
                        }
                        else {
                            if (nearbyLoading.value != true) {
                                viewModel.getNearbyAirports()
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
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