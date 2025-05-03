package com.example.ft.search.search_flights

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.ft.R
import java.util.Calendar
import java.util.Date

//диалог для выбора даты
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightDatePicker(
    date: Date, //дата
    onDateSelected: (Date) -> Unit, //функция выбора даты
    onDismiss: () -> Unit //функция закрытия диалога
) {
    //состояние для диалога
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = date.time,
        selectableDates = FlightSelectableDates()
    )

    //сам диалог
    DatePickerDialog(
        onDismissRequest = onDismiss,
        //кнопка "Ок"
        confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let {
                    onDateSelected(Date(it))
                }
                onDismiss()
            }) {
                Text(text = stringResource(R.string.ok))
            }
        },
        //кнопка "Отмена"
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    ) {
        //календарь
        DatePicker(state = datePickerState)
    }
}

//допустимый промежуток дат - начиная неделей позднее текущей даты
@OptIn(ExperimentalMaterial3Api::class)
class FlightSelectableDates: SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        //добавляем к текущей дате неделю
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, 7)
        val comparable = calendar.timeInMillis
        //дата должна быть позднее
        return utcTimeMillis > comparable
    }
}