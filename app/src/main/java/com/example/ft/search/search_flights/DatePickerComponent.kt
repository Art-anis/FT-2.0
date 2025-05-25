package com.example.ft.search.search_flights

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ft.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//компонент для выбора даты
@Composable
fun DatePickerComponent(
    date: Date, //сама дата
    updateDate: (Date) -> Unit //функция обновления даты после ее выбора
) {
    //состояние для диалога
    var datePickerDialogState by rememberSaveable { mutableStateOf(false) }
    //выбор даты
    Text(
        //если даты нет, то выводим плейсхолдер, иначе выводим дату в формате "MMM dd, yyyy"
        text = SimpleDateFormat(stringResource(R.string.date_format), Locale.ENGLISH).format(date),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .background(Color.White)
            .padding(8.dp)
            //по клику переходим в выбор даты
            .clickable {
                datePickerDialogState = true
            },
    )
    //отображаем диалог для выбора даты
    if (datePickerDialogState) {
        FlightDatePicker(
            date = date,
            onDateSelected = { newDate ->
                updateDate(newDate)
            },
            onDismiss = {
                datePickerDialogState = false
            }
        )
    }
}