package com.example.ft.flight_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

//данные об аэропорту на карточке рейса
@Composable
fun FlightCardAirportData(
    modifier: Modifier = Modifier, //modifier
    city: String, //название города
    iataCode: String, //iata код аэропорта
    alignment: String,
    time: String //время
) {
    //контейнер
    Column(
        modifier = modifier
    ) {
        //код и название города
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = if (alignment == "start") Arrangement.Start else Arrangement.End
        ) {
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
                modifier = Modifier.fillMaxWidth(),
                text = time,
                textAlign = if (alignment == "start") TextAlign.Start else TextAlign.End
            )
        }
    }
}