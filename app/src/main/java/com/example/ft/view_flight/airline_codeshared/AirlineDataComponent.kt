package com.example.ft.view_flight.airline_codeshared

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import com.example.view_flight.util.AirlineData

//блок об авиалинии
fun LazyListScope.airlineDataComponent(
    data: AirlineData, //данные
    logo: AsyncImagePainter, //логотип
    airlineStatus: String //статус авиалинии - главная или кодшеринг
) {
    //преобразовываем имя авиалинии - делаем каждую первую букву заглавной
    val airlineName = data.airlineName.split(" ")
        .joinToString(" ") { word -> word.replaceFirstChar { it.uppercase() } }

    //сам элемент
    item {
        Column(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = Color.Black
                )
                .padding(8.dp)
        ) {
            //логотип, если есть
            if (data.airlineIata.isNotEmpty()) {
                Image(
                    painter = logo,
                    contentDescription = null
                )
            }
            //номер рейса в авиалинии
            Text(data.flightNumber)
            //название авиалинии
            Text(airlineName)
            //статус авиалинии
            Text(airlineStatus)
        }
    }
}