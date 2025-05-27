package com.example.ft.search.search_flights

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ft.R
import com.example.ft.util.DestinationType
import com.example.search_airports.util.AirportUIModel
import java.util.Date

//поиск рейсов
@Composable
fun FlightSearchScreen(
    viewModel: FlightsSearchViewModel, //общая с AirportSearchScreen viewmodel
    onNavigateToAirportSearch: (DestinationType) -> Unit, //функция перехода на экран поиска аэропортов
    onLaunchSearch: (AirportUIModel, AirportUIModel, Date) -> Unit //функция запуска поиска рейсов
) {
    //состояние фрагмента поиска
    val searchData by viewModel.searchModel.observeAsState()

    val searchHistory by viewModel.searchHistory.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.getHistory()
    }
    //коренной элемент
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        //меню для поиска
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Blue)
                .padding(top = 32.dp)
        ) {
            searchData?.let {
                //поля поиска для аэропортов
                AirportSearchComponent(
                    departure = it.departure,
                    arrival = it.arrival,
                    onNavigateToAirportSearch = onNavigateToAirportSearch,
                    clearDeparture = viewModel::clearDeparture,
                    clearArrival = viewModel::clearArrival,
                )

                //поле выбора даты
                DatePickerComponent(
                    date = it.date,
                    updateDate = viewModel::updateDate
                )


                //кнопка "Поиск"
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .padding(bottom = 16.dp),
                    //включена, только если были выбраны аэропорты вылета и прибытия
                    enabled = !searchData?.departure?.iataCode.isNullOrEmpty() &&
                            !searchData?.arrival?.iataCode.isNullOrEmpty(),
                    //при нажатии передаем все необходимые данные в активность
                    onClick = {
                        val model = searchData!!

                        //запускаем процедуру поиска рейсов
                        onLaunchSearch(
                            model.departure,
                            model.arrival,
                            model.date
                        )
                    }
                ) {
                    Text(stringResource(R.string.search_btn))
                }
            }

        }
        //отображение истории поиска
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (searchHistory.isNullOrEmpty()) {
                item {
                    Text("You haven't searched anything yet!")
                }
            }
            else {
                items(searchHistory!!) {
                    Row(modifier = Modifier.fillMaxWidth())
                    {
                        Icon(Icons.Filled.History, contentDescription = null)
                        Text(it)
                    }
                }
            }
        }
    }
}