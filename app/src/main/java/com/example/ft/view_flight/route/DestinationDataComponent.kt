package com.example.ft.view_flight.route

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.example.view_flight.util.DestinationData

//компонент с данными о месте вылета/прибытия
@Composable
fun DestinationDataComponent(
    destination: DestinationData, //данные о месте
    alignment: Alignment.Horizontal //выравнивание
) {
    Column(
        horizontalAlignment = alignment
    ) {
        //название города
        Text(text = destination.cityName)
        //iata код
        Text(text = destination.iata.uppercase())
        //время вылета/прибытия
        Text(text = destination.time)
    }
}