package com.example.ft.view_flight.terminal_gate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TermAndGateRow(
    departureTerminal: String,
    departureGate: String,
    arrivalTerminal: String,
    arrivalGate: String
) {
    //данные о терминалах и выходах
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 64.dp)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        //терминал и выход в аэропорте вылета
        TermAndGateComponent(
            terminal = departureTerminal,
            gate = departureGate
        )
        //терминал и выход в аэропорте прибытия
        TermAndGateComponent(
            terminal = arrivalTerminal,
            gate = arrivalGate
        )
    }
}