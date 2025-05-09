package com.example.ft.view_flight.terminal_gate

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ft.R

//данные о терминале и выходе
@Composable
fun TermAndGateComponent(
    terminal: String,
    gate: String
) {
    Row {
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
                text = if (terminal.isEmpty()) stringResource(R.string.empty_field)
                else terminal,
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
                text = if (gate.isEmpty()) stringResource(R.string.empty_field)
                else gate,
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