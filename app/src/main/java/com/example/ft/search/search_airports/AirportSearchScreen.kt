package com.example.ft.search.search_airports

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.platform.LocalContext
import com.example.ft.R
import com.example.ft.util.DestinationType
import com.example.search_flights.FlightsSearchViewModel

//экран поиска аэропортов
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AirportSearchScreen(
    viewModel: FlightsSearchViewModel,
    airportType: DestinationType,
    onNavigateBack: () -> Unit //обратная навигация
) {

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
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            //кнопка "Назад"
            IconButton(onClick = {
                viewModel.clearSearch()
                onNavigateBack()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
            //поисковое поле
            TextField(
                //размеры
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 32.dp),
                //значение
                value = query.text.toString(),
                //в одну строку
                singleLine = true,
                //определяем действие при нажатии Enter
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                //обновление состояния текста
                onValueChange = {
                    query.edit { replace(0, length, it) }
                    viewModel.searchAirport(query.text.toString())
                },
                //ведущая иконка
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                },
                //хвостовая иконка для очистки текста
                trailingIcon = {
                    IconButton(onClick = {
                        query.clearText()
                        viewModel.clearSearch()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = null
                        )
                    }
                },
                //название
                label = {
                    Text(stringResource(R.string.search_airport_label))
                },
                //плейсхолдер
                placeholder = {
                    Text(stringResource(R.string.search_airport_placeholder))
                }
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        //результаты и история поиска
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            //заголовок истории поиска
            item {
                Text(stringResource(R.string.search_history_header))
            }
            //история поиска, если она есть
            airports.value?.searchHistory?.let { history ->
                items(history) { airport ->
                    val context = LocalContext.current
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 32.dp, top = 16.dp, bottom = 16.dp)
                            .clickable {
                                //обновляем соответствующее поле в viewmodel
                                when (airportType) {
                                    DestinationType.ARRIVAL -> {
                                        if (!viewModel.setArrival(airport)) {
                                            //выводим тост, если нельзя выбрать этот аэропорт
                                            Toast.makeText(
                                                context,
                                                context.getString(R.string.departure_repeat),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        else {
                                            //очищаем поиск
                                            viewModel.clearSearch()
                                            onNavigateBack()
                                        }
                                    }

                                    DestinationType.DEPARTURE -> {
                                        if (!viewModel.setDeparture(airport)) {
                                            Toast.makeText(
                                                context,
                                                context.getString(R.string.arrival_repeat),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        else {
                                            //очищаем поиск
                                            viewModel.clearSearch()
                                            onNavigateBack()
                                        }
                                    }
                                }
                            }
                    ) {
                        //иконка для отличия истории от результатов
                        Icon(
                            imageVector = Icons.Filled.History,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        //название аэропорта и города
                        Text(
                            text = "${airport.airportName}, ${airport.cityName}"
                        )
                    }
                    HorizontalDivider()
                }
            }
            if (airports.value?.searchHistory.isNullOrEmpty()) {
                item {
                    Text(stringResource(R.string.empty_search_history))
                }
            }
            //заголовок для результатов поиска
            item {
                Text(
                    text = stringResource(R.string.search_results_header),
                    modifier = Modifier.padding(top = 32.dp, bottom = 16.dp)
                )
            }
            //результаты поиска, если они есть
            airports.value?.searchResult?.let { result ->
                items(result) { airport ->
                    //очередной аэропорт
                    AirportItem(
                        airport = airport,
                        //при выборе
                        onClick = {
                            //обновляем соответствующее поле в viewmodel
                            when(airportType) {
                                DestinationType.ARRIVAL -> {
                                    viewModel.setArrival(airport)
                                }
                                DestinationType.DEPARTURE -> {
                                    viewModel.setDeparture(airport)
                                }
                            }
                            //очищаем поиск
                            viewModel.clearSearch()
                            onNavigateBack()
                        }
                    )
                    HorizontalDivider()
                }
            }
            if (airports.value?.searchResult.isNullOrEmpty() && query.text.isNotEmpty() && airports.value?.loading == false) {
                item {
                    Text(stringResource(R.string.empty_search_result))
                }
            }
        }
    }

    //при переходе назад очищаем результаты поиска
    BackHandler {
        viewModel.clearSearch()
        onNavigateBack()
    }
}