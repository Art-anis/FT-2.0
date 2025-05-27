package com.example.ft.view_flight.terminal_gate

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ft.R
import com.example.ft.util.DestinationType

//данные о терминале и выходе
@Composable
fun TermAndGateComponent(
    terminal: String,
    gate: String,
    type: DestinationType
) {
    Column(
        modifier = Modifier.fillMaxWidth(if (type == DestinationType.DEPARTURE) 0.5f else 1f)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 8.dp),
            text = when(type) {
                DestinationType.DEPARTURE -> "Departure"
                DestinationType.ARRIVAL -> "Arrival"
            },
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            //терминал
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //заголовок
                Text(
                    text = stringResource(R.string.terminal_header),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = terminal.ifEmpty { stringResource(R.string.empty_field) },
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = Color.Black
                        )
                        .width(40.dp),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            //номер выхода
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //заголовок
                Text(
                    text = stringResource(R.string.gate_header),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = gate.ifEmpty { stringResource(R.string.empty_field) },
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = Color.Black
                        )
                        .width(40.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}