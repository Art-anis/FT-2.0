package com.example.ft.search.search_flights

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ft.R
import com.example.ft.util.DestinationType
import com.example.search_airports.util.AirportUIModel

//поля поиска для аэропортов
@Composable
fun AirportSearchComponent(
    departure: AirportUIModel, //состояние для аэропорта вылета
    arrival: AirportUIModel, //состояние для аэропорта прибытия
    onNavigateToAirportSearch: (DestinationType) -> Unit, //функция перехода на экран поиска аэропортов
    clearDeparture: () -> Unit, //функция очистки аэропорта вылета
    clearArrival: () -> Unit //функция очистки аэропорта прибытия
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //указание аэропорта отправления
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .background(Color.White)
                .padding(8.dp)
                .clickable {
                    //по клику переходим в поиск аэропорта
                    onNavigateToAirportSearch(DestinationType.DEPARTURE)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            //название аэропорта вылета и его города
            val departureAirportName = departure.airportName
            val departureCityName = departure.cityName
            Text(
                //выводим плейсхолдер, если аэропорт еще не выбран
                text = if (departureAirportName.isEmpty() || departureCityName.isEmpty())
                    stringResource(R.string.search_departure_label)
                else "$departureAirportName, $departureCityName"
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                modifier = Modifier.size(20.dp),
                onClick = clearDeparture
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = null
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .background(Color.White)
                .padding(8.dp)
                //по клику переходим в поиск аэропорта
                .clickable {
                    onNavigateToAirportSearch(DestinationType.ARRIVAL)
                }
        ) {
            //название аэропорта прибытия и его города
            val arrivalAirportName = arrival.airportName
            val arrivalCityName = arrival.cityName
            //указание аэропорта прибытия
            Text(
                //выводим плейсхолдер, если аэропорт еще не выбран
                text = if (arrivalAirportName.isEmpty() || arrivalCityName.isEmpty())
                    stringResource(R.string.search_arrival_label) else
                    "$arrivalAirportName, $arrivalCityName"
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                modifier = Modifier.size(20.dp),
                onClick = clearArrival
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = null
                )
            }
        }
    }
}