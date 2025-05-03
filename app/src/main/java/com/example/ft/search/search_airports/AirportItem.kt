package com.example.ft.search.search_airports

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.search_airports.util.AirportUIModel

//элемент списка аэропортов
@Composable
fun AirportItem(
    airport: AirportUIModel,
    onClick: () -> Unit
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            onClick()
        }
    ) {
        Text(text = "${airport.iataCode} - ${airport.airportName}")
        Text(text = "${airport.cityName}, ${airport.countryName}")
    }
}