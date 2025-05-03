package com.example.ft.search.search_flights

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ft.R

//выбор типа даты - дата прибытия или вылета
@Composable
fun DateOptionPickerComponent(
    selected: String, //значение состояния выбранной кнопки
    updateSelection: (String) -> Unit //функция обновления выбора
) {
    //опции
    val radioButtonOptions = listOf(stringResource(R.string.date_of_departure),
        stringResource(R.string.date_of_arrival))

    //чекбоксы для даты - это дата вылета/прибытия
    Column(
        modifier = Modifier
            .padding(start = 32.dp, top = 16.dp, bottom = 16.dp)
            .selectableGroup()
    ) {
        //кнопка "Дата вылета"
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.selectable(
                selected = selected == radioButtonOptions[0],
                onClick = {
                    updateSelection(radioButtonOptions[0])
                }
            )
        ) {
            //сама кнопка
            RadioButton(
                selected = selected == radioButtonOptions[0],
                onClick = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            //подпись
            Text(text = stringResource(R.string.date_of_departure))
        }
        Spacer(modifier = Modifier.height(8.dp))
        //кнопка "Дата прибытия"
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.selectable(
                selected = selected == radioButtonOptions[1],
                onClick = {
                    updateSelection(radioButtonOptions[1])
                }
            )
        ) {
            //сама кнопка
            RadioButton(
                selected = selected == radioButtonOptions[1],
                onClick = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            //подпись
            Text(text = stringResource(R.string.date_of_arrival))
        }
    }
}