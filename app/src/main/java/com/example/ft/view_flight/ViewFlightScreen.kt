package com.example.ft.view_flight

import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ft.R
import com.example.ft.navigation.FlightData
import com.example.view_flight.ViewFlightViewModel
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

//экран просмотра отдельного рейса
@Composable
fun ViewFlightScreen(
    flightData: FlightData //данные о рейсе
) {
    //viewmodel
    val viewModel = koinViewModel<ViewFlightViewModel>()

    //состояние данных о рейсе
    val flight = viewModel.flightData.observeAsState()

    //получаем данные из репозитория
    LaunchedEffect(Unit) {
        viewModel.loadAirport(
            flightNumber = flightData.flightNumber,
            departure = flightData.departure,
            arrival = flightData.arrival
        )
    }

    //отображаем данные о рейсе
    flight.value?.let { data ->
        val scrollState = rememberScrollState()
        //контейнер
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = scrollState)
        ) {
            //данные о месте вылета и прибытия
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp)
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                //место вылета
                Column(
                    horizontalAlignment = Alignment.Start
                ) {
                    //название города
                    Text(text = data.departure.cityName)
                    //iata код
                    Text(text = data.departure.iata.uppercase())
                    //время вылета
                    Text(text = data.departure.time)
                }
                Icon(
                    imageVector = Icons.Filled.Flight,
                    contentDescription = null,
                    modifier = Modifier
                        .rotate(90f)
                        .size(50.dp)
                )
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    //название города
                    Text(text = data.arrival.cityName)
                    //iata код
                    Text(text = data.arrival.iata.uppercase())
                    //время вылета
                    Text(text = data.arrival.time)
                }
            }
            //данные о терминалах и выходах
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 64.dp)
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                //терминал и выход в аэропорте вылета
                Row {
                    //терминал
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        //заголовок
                        Text(
                            text = stringResource(R.string.terminal_header),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = if (data.departure.terminal.isEmpty()) "--" else data.departure.terminal,
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = Color.Black
                                )
                                .width(40.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    //номер выхода
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        //заголовок
                        Text(
                            text = stringResource(R.string.gate_header),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = if (data.departure.gate.isEmpty()) stringResource(R.string.empty_field)
                            else data.departure.gate,
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = Color.Black
                                )
                                .width(40.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                //терминал и выход в аэропорте прибытия
                Row {
                    //терминал
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        //заголовок
                        Text(
                            text = stringResource(R.string.terminal_header),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = if (data.arrival.terminal.isEmpty()) "--" else data.arrival.terminal,
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = Color.Black
                                )
                                .width(40.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    //номер выхода
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        //заголовок
                        Text(
                            text = stringResource(R.string.gate_header),
                            modifier = Modifier.padding(bottom = 8.dp))
                        Text(
                            text = if (data.arrival.gate.isEmpty()) "--" else data.arrival.gate,
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = Color.Black
                                )
                                .width(40.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            //данные об авиалинии и кодшеринге, если он есть
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 64.dp)
                .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Airline and codeshared",
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                LazyRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        val airlineName = data.airline.airlineName.split(" ").
                        joinToString(" ") { word -> word.replaceFirstChar { it.uppercase() }}
                        Column(
                            modifier = Modifier.border(
                                    width = 1.dp,
                                    color = Color.Black
                                )
                                .padding(8.dp)
                        ) {
                            Text(data.airline.flightNumber)
                            Text(airlineName)
                        }
                    }
                    data.codeshared?.let { codeshared ->
                        item {
                            val airlineName = codeshared.airlineName.split(" ").
                                joinToString(" ") { word -> word.replaceFirstChar { it.uppercase() }}
                            Column(
                                modifier = Modifier.padding(8.dp)
                                    .border(
                                        width = 1.dp,
                                        color = Color.Black
                                    )
                            ) {
                                Text(codeshared.flightNumber)
                                Text(
                                    text = airlineName
                                )
                            }
                        }
                    }
                }
            }
            Text(
                text = "Your aircraft is ${data.aircraft}",
                modifier = Modifier.padding(
                    top = 16.dp,
                    start = 16.dp
                )
            )
        }
    } ?: Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }

}