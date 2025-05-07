package com.example.ft.flight_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.flight_list.util.FlightItemUIModel
import com.example.ft.navigation.FlightData

//карточка с данными о рейсе
@Composable
fun FlightCard(
    flight: FlightItemUIModel, //рейс
    departureCity: String, //название города вылета
    arrivalCity: String, //название города прибытия
    onNavigateToViewFlight: (FlightData) -> Unit //функция перехода на экран просмотра рейса
) {
    //контейнер
    Card(
        modifier = Modifier.fillMaxWidth()
            .padding(
                horizontal = 32.dp,
                vertical = 16.dp
            )
            //при нажатии на карточку переходим на экран просмотра
            .clickable {
                onNavigateToViewFlight(
                    FlightData(
                        flightNumber = flight.flightNumber.uppercase(),
                        departure = departureCity,
                        arrival = arrivalCity
                    )
                )
            }
    ){
    //текст с номером рейса
    if (flight.flightNumber.isNotEmpty()) {
            Text(
                text = flight.flightNumber,
                modifier = Modifier.padding(
                    start = 8.dp,
                    bottom = 16.dp,
                    top = 8.dp
                )
            )
        }
        //контейнер для данных об аэропортах
        Box(modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .padding(bottom = 8.dp)
        ) {
            //аэропорт вылета
            FlightCardAirportData(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 8.dp),
                city = departureCity,
                iataCode = flight.departureIata,
                time = flight.departureTime
            )
            //аэропорт прибытия
            FlightCardAirportData(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp),
                city = arrivalCity,
                iataCode = flight.arrivalIata,
                time = flight.arrivalTime
            )
        }
    }
}