package com.example.ft.view_flight.route

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.example.view_flight.util.DestinationData

//ряд с данными о местах вылета и прибытия
@Composable
fun RouteRow(
    departure: DestinationData,
    arrival: DestinationData
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        //место вылета
        DestinationDataComponent(
            destination = departure,
            alignment = Alignment.Start
        )
        //иконка самолета
        Icon(
            imageVector = Icons.Filled.Flight,
            contentDescription = null,
            modifier = Modifier
                .rotate(90f)
                .size(50.dp)
        )
        //место прибытия
        DestinationDataComponent(
            destination = arrival,
            alignment = Alignment.End
        )
    }
}