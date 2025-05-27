package com.example.ft.search.search_airports

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.search_airports.util.AirportUIModel

const val FLAGS_API = "https://flagsapi.com/"

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
        HorizontalDivider()
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val model = FLAGS_API + airport.countryCode.uppercase() + "/flat/64.png"
            AsyncImage(
                model = model,
                contentDescription = null,
                modifier = Modifier.size(100.dp)
                    .padding(horizontal = 16.dp)
            )
            Column {
                //главная информация - название аэропорта и его iata код
                Text(text = "${airport.iataCode} - ${airport.airportName}")
                //дополнительная информация - название города и страны
                Text(text = "${airport.cityName}, ${airport.countryName}")
            }
        }
        HorizontalDivider()
    }
}