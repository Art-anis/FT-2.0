package com.example.ft.airport_timetable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ft.R
import com.example.ft.flight_list.FlightCard
import com.example.ft.navigation.FlightData
import com.example.ft.search.search_flights.DatePickerComponent
import java.util.Calendar
import java.util.Date

//экран выбора аэропорта для просмотра расписания
@Composable
fun AirportScreen(
    viewModel: AirportTimetableViewModel,
    onNavigateToAirportSearch: () -> Unit,
    onNavigateToViewFlight: (FlightData) -> Unit
) {
    val airport by viewModel.selectedAirport.observeAsState()
    var airportName = airport?.airportName
    var cityName = airport?.cityName

    val calendar = Calendar.getInstance()
    calendar.time = Date()
    calendar.add(Calendar.DATE, 8)
    var date by rememberSaveable { mutableStateOf(calendar.time) }

    val timetable by viewModel.timetable.observeAsState()
    val loadingTimetable by viewModel.loadingTimetable.observeAsState()

    Column {
        //указание аэропорта
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .background(Color.White)
                .padding(8.dp)
                .clickable {
                    //по клику переходим в поиск аэропорта
                    onNavigateToAirportSearch()
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                //выводим плейсхолдер, если аэропорт еще не выбран
                text = if (airportName.isNullOrEmpty() || cityName.isNullOrEmpty())
                    stringResource(R.string.search_departure_label)
                else "$airportName, $cityName"
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                modifier = Modifier.size(20.dp),
                onClick = {
                    airportName = ""
                    cityName = ""
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = null
                )
            }
        }
        DatePickerComponent(
            date = date,
            updateDate = {
                date = it
            }
        )

        Button(
            enabled = airport != null && !airport?.airportName.isNullOrEmpty(),
            onClick = {
                viewModel.getTimetable(airport!!.iataCode, date.time)
            }
        ) {
            Text("Get timetable")
        }

        LazyColumn(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            if (loadingTimetable == false) {
                timetable?.let {
                    if (it.isEmpty()) {
                        item {
                            Text("Sorry, we couldn't find anything!")
                        }
                    } else {
                        items(it) { data ->
                            FlightCard(
                                flight = data.first,
                                departureCity = cityName ?: "",
                                arrivalCity = data.second,
                                date = date.time,
                                onNavigateToViewFlight = onNavigateToViewFlight,
                                fromTimetable = true
                            )
                        }
                    }
                } ?: item {
                    Text("The timetable will be displayed here!")
                }
            }
            else {
                item {
                    CircularProgressIndicator()
                }
            }
        }
    }
}