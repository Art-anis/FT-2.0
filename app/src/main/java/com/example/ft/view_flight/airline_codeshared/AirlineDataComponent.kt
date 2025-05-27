package com.example.ft.view_flight.airline_codeshared

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import com.example.ft.R
import com.example.view_flight.util.AirlineData


//блок об авиалинии
@Composable
fun AirlineDataComponent(
    data: AirlineData, //данные
    logo: AsyncImagePainter, //логотип
    airlineStatus: String //статус авиалинии - главная или кодшеринг
) {
    //преобразовываем имя авиалинии - делаем каждую первую букву заглавной
    val airlineName = data.airlineName.split(" ")
        .joinToString(" ") { word -> word.replaceFirstChar { it.uppercase() } }

    //сам элемент
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(if (airlineStatus == stringResource(R.string.main_airline)) 0.5f else 1f)
    ) {
        //статус авиалинии
        Text(
            text = airlineStatus,
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 8.dp),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            //логотип, если есть
            if (data.airlineIata.isNotEmpty()) {
                Image(
                    painter = logo,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .fillMaxWidth(),
                    contentDescription = null
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                //номер рейса в авиалинии
                Text(data.flightNumber)
                Spacer(modifier = Modifier.width(16.dp))
                //название авиалинии
                Text(airlineName)
            }
        }
    }
}