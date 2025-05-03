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
    airport: AirportUIModel, //модель аэропорта
    onSelected: () -> Unit //функция для выбора аэропорта
) {
    //контейнер
    Column(modifier = Modifier
        .fillMaxWidth()
        //кликабельный
        .clickable {
            onSelected()
        }
    ) {
        //главная информация - название аэропорта и его iata код
        Text(text = "${airport.iataCode} - ${airport.airportName}")
        //дополнительная информация - название города и страны
        Text(text = "${airport.cityName}, ${airport.countryName}")
    }
}