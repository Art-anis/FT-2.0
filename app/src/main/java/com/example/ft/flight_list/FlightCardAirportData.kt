package com.example.ft.flight_list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

//данные об аэропорту на карточке рейса
@Composable
fun FlightCardAirportData(
    modifier: Modifier = Modifier, //modifier
    city: String, //название города
    iataCode: String, //iata код аэропорта
    time: String //время
) {
    //контейнер
    Column(
        modifier = modifier
    ) {
        //код и название города
        Row {
            //код iata
            if (iataCode.isNotEmpty()) {
                Text(
                    text = iataCode.uppercase(),
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
            //название города
            Text(
                text = city
            )
        }
        //время вылета/прибытия
        if (time.isNotEmpty()) {
            Text(
                text = time
            )
        }
    }
}