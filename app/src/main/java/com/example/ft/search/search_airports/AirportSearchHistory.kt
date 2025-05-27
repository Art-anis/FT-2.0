package com.example.ft.search.search_airports

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ft.R
import com.example.ft.util.DestinationType
import com.example.ft.util.SubHeader
import com.example.search_airports.util.AirportUIModel

//история поиска аэропортов
@Composable
fun AirportSearchHistory(
    history: List<AirportUIModel>, //история поиска
    airportType: DestinationType?, //тип аэропорта
    onNavigateBack: () -> Unit, //функция возвращения назад
    viewModel: AirportsSearchViewModel, //viewmodel
    setAirport: (AirportUIModel) -> Boolean //функция выбора аэропорта
) {
    Column {
        //заголовок истории поиска
        SubHeader(
            text = stringResource(R.string.search_history_header),
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        //история поиска, если она есть
        history.forEach { airport ->
            val context = LocalContext.current
            HorizontalDivider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
                    .clickable {
                        //если установка завершилась неудачно (т.е. в другом поле уже выбран этот аэропорт
                        if (!setAirport(airport)) {
                            //выводим соответствующий тост
                            when(airportType) {
                                DestinationType.DEPARTURE -> Toast.makeText(context,
                                    context.getString(R.string.departure_repeat), Toast.LENGTH_SHORT).show()
                                DestinationType.ARRIVAL -> Toast.makeText(context,
                                    context.getString(R.string.arrival_repeat), Toast.LENGTH_SHORT).show()
                                else -> {}
                            }
                        }
                        //иначе выбор прошел успешно
                        else {
                            //обновляем дату выбора аэропорта
                            viewModel.updateDate(airport.iataCode)
                            //очищаем поиск
                            viewModel.clearSearch()
                            //возвращаемся назад
                            onNavigateBack()
                        }
                    }
            ) {
                //иконка для отличия истории от результатов
                Icon(
                    imageVector = Icons.Filled.History,
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                //название аэропорта и города
                Text(
                    text = "${airport.airportName}, ${airport.cityName}"
                )
            }
            HorizontalDivider()
        }
        //если история пуста
        if (history.isEmpty()) {
            //выводим сообщение о пустой истории
            Text(
                text = stringResource(R.string.empty_search_history),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
        }
    }
}