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
import com.example.flight_list.FlightListViewModel
import com.example.ft.R
import com.example.ft.navigation.FlightListSearchData
import org.koin.androidx.compose.koinViewModel

//просмотр результата поиска рейсов
@Composable
fun FlightListScreen(
    searchData: FlightListSearchData, //данные для поиска
    departureCityName: String, //название города вылета
    arrivalCityName: String //название города прибытия
) {

    //viewmodel
    val viewModel = koinViewModel<FlightListViewModel>()

    //состояние результата поиска
    val flightsList = viewModel.flightList.observeAsState()
    val loading = viewModel.loading.observeAsState()

    //запускаем поиск
    LaunchedEffect(Unit) {
        //преобразовываем данные в нужный формат
        val type = searchData.type.value
        //запускаем поиск
        viewModel.searchFlights(
            departure = searchData.departure.iataCode,
            arrival = searchData.arrival.iataCode,
            type = type,
            date = searchData.date
        )
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
                    arrivalCity = arrivalCityName
                )
            }
        }
    }
}