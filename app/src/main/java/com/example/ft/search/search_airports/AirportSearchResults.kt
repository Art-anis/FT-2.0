package com.example.ft.search.search_airports

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ft.R
import com.example.ft.util.DestinationType
import com.example.search_airports.util.AirportUIModel

//результаты поиска
fun LazyListScope.airportSearchResults(
    searchResult: List<AirportUIModel>, //список аэропортов
    loading: Boolean, //состояние загрузки
    query: TextFieldState, //состояние поисковой строки
    airportType: DestinationType?, //тип аэропорта
    onNavigateBack: () -> Unit, //функция возвращения назад
    viewModel: AirportsSearchViewModel, //viewmodel
    setAirport: (AirportUIModel) -> Boolean //функция выбора аэропорта
) {
    //сам список
    items(searchResult) { airport ->
        val context = LocalContext.current
        //очередной аэропорт
        AirportItem(
            airport = airport,
            //при выборе
            onSelected = {
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
        )
    }

    //если результат поиска пустой и при этом поле поиска не пусто и не идет процесс загрузки
    if (searchResult.isEmpty() && query.text.isNotEmpty() && !loading) {
        //выводим текст о том, что ничего не нашли
        item {
            Text(
                text = stringResource(R.string.empty_search_result),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }

    if (query.text.isEmpty()) {
        item {
            Text(
                text = stringResource(R.string.start_searching_prompt),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}