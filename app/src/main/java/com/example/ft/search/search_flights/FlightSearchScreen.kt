package com.example.ft.search.search_flights

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ft.R
import com.example.ft.util.DestinationType
import com.example.search_flights.FlightsSearchViewModel
import java.text.SimpleDateFormat
import java.util.Locale

//поиск рейсов
@Composable
fun FlightSearchScreen(
    viewmodel: FlightsSearchViewModel,
    onNavigateToAirportSearch: (DestinationType) -> Unit
) {
    //состояние фрагмента поиска
    val searchData = viewmodel.searchModel.observeAsState()
    //коренной элемент
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        //меню для поиска
        Column(
            modifier = Modifier
                .fillMaxHeight(0.4f)
                .fillMaxWidth()
                .background(color = Color.Blue)
                .padding(top = 32.dp)
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
                    val departureAirportName = searchData.value?.departure?.airportName
                    val departureCityName = searchData.value?.departure?.cityName
                    Text(
                        //выводим плейсхолдер, если аэропорт еще не выбран
                        text = if (departureAirportName.isNullOrEmpty() || departureCityName.isNullOrEmpty())
                            stringResource(R.string.search_departure_label)
                        else "$departureAirportName, $departureCityName"
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        modifier = Modifier.size(20.dp),
                        onClick = {
                            viewmodel.clearDeparture()
                        }
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
                    val arrivalAirportName = searchData.value?.arrival?.airportName
                    val arrivalCityName = searchData.value?.arrival?.cityName
                    //указание аэропорта прибытия
                    Text(
                        //выводим плейсхолдер, если аэропорт еще не выбран
                        text = if (arrivalAirportName.isNullOrEmpty() || arrivalCityName.isNullOrEmpty())
                            stringResource(R.string.search_arrival_label) else
                                "$arrivalAirportName, $arrivalCityName"
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        modifier = Modifier.size(20.dp),
                        onClick = {
                            viewmodel.clearArrival()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = null
                        )
                    }
                }
            }

            val date = searchData.value?.date
            var datePickerDialogState by rememberSaveable { mutableStateOf(false) }
            //выбор даты
            Text(
                //если даты нет, то выводим плейсхолдер, иначе выводим дату в формате "MMM dd, yyyy"
                text = date?.let {
                    SimpleDateFormat(stringResource(R.string.date_format), Locale.getDefault()).format(it) } ?:
                    stringResource(R.string.search_date_label),
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
                date?.let {
                    FlightDatePicker(
                        date = date,
                        onDateSelected = { newDate ->
                            viewmodel.updateDate(newDate)
                        },
                        onDismiss = {
                            datePickerDialogState = false
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            //чекбоксы для даты - это дата вылета/прибытия
            Column(
                modifier = Modifier
                    .padding(start = 32.dp)
                    .selectableGroup()
            ) {
                //опции
                val radioButtonOptions = listOf(stringResource(R.string.date_of_departure),
                    stringResource(R.string.date_of_arrival))
                
                //состояние, обозначающее, какая кнопка выбрана
                var selectedButton by rememberSaveable { mutableStateOf(radioButtonOptions[0]) }
                //кнопка "Дата вылета"
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.selectable(
                        selected = selectedButton == radioButtonOptions[0],
                        onClick = {
                            selectedButton = radioButtonOptions[0]
                        }
                    )
                ) {
                    //сама кнопка
                    RadioButton(
                        selected = selectedButton == radioButtonOptions[0],
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
                        selected = selectedButton == radioButtonOptions[1],
                        onClick = {
                            selectedButton = radioButtonOptions[1]
                        }
                    )
                ) {
                    //сама кнопка
                    RadioButton(
                        selected = selectedButton == radioButtonOptions[1],
                        onClick = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    //подпись
                    Text(text = stringResource(R.string.date_of_arrival))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            //кнопка "Поиск"
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                onClick = {}
            ) {
                Text(stringResource(R.string.search_btn))
            }
        }
        //отображение истории поиска
        Column(
            modifier = Modifier.fillMaxHeight(0.6f)
        ) {

        }
    }
}