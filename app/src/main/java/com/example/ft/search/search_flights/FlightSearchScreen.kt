package com.example.ft.search.search_flights

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ft.R
import com.example.ft.util.DestinationType
import com.example.ft.util.SubHeader
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

    //история поиска
    val searchHistory by viewModel.searchHistory.observeAsState()

    //получаем историю поиска
    LaunchedEffect(Unit) {
        viewModel.getHistory()
    }
    //коренной элемент
    Column(
        modifier = Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        //меню для поиска
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = colorResource(R.color.light_blue))
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
                        .padding(vertical = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.button_color)),
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

        SubHeader(
            text = stringResource(R.string.search_history_header),
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = 16.dp)
        )
        //отображение истории поиска
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (searchHistory.isNullOrEmpty()) {
                Text(
                    text = stringResource(R.string.empty_history_hint),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
            else {
                searchHistory!!.forEach { item ->
                    HorizontalDivider()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.History,
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(item)
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}