package com.example.ft.flight_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.ft.R
import com.example.ft.navigation.FlightData
import com.example.ft.navigation.FlightListSearchData
import org.koin.androidx.compose.koinViewModel

//просмотр результата поиска рейсов
@Composable
fun FlightListScreen(
    searchData: FlightListSearchData, //данные для поиска
    departureCityName: String, //название города вылета
    arrivalCityName: String, //название города прибытия
    onNavigateToViewFlight: (FlightData) -> Unit //функция перехода на экран просмотра рейса
) {

    //viewmodel
    val viewModel = koinViewModel<FlightListViewModel>()

    //состояние результата поиска
    val flightsList = viewModel.flightList.observeAsState()
    val loading = viewModel.loading.observeAsState()

    //запускаем поиск
    LaunchedEffect(Unit) {
        //если до этого не искали или результат пустой
        if (flightsList.value.isNullOrEmpty()) {
            //запускаем поиск
            viewModel.searchFlights(
                departure = searchData.departure,
                arrival = searchData.arrival,
                date = searchData.date
            )
        }
    }

    //если загрузка еще идет
    if (loading.value == true) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
            Text(stringResource(R.string.searching_flights))
        }
    }
    //если данные уже загружены
    else {
        //отображаем результаты поиска
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(flightsList.value ?: emptyList()) { flightData ->
                //карточка с данными рейса
                FlightCard(
                    flight = flightData,
                    departureCity = departureCityName,
                    arrivalCity = arrivalCityName,
                    date = searchData.date,
                    onNavigateToViewFlight
                )
            }

            //сообщение о том, что ничего не нашли
            if (flightsList.value.isNullOrEmpty()) {
                item {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(stringResource(R.string.flight_empty_search_result))
                    }
                }
            }
        }
    }
}