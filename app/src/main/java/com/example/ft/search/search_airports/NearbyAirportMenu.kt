package com.example.ft.search.search_airports

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ft.R
import com.example.ft.util.DestinationType
import com.example.ft.util.SubHeader
import com.example.search_airports.util.AirportUIModel


//выбор аэропорта рядом с пользователем
@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NearbyAirportMenu(
    nearbyAirports: List<AirportUIModel>?,
    nearbyLoading: Boolean?,
    airportType: DestinationType?,
    setAirport: (AirportUIModel) -> Boolean,
    viewModel: AirportsSearchViewModel,
    onNavigateBack: () -> Unit
) {
    //состояние меню
    var expanded by rememberSaveable { mutableStateOf(false) }

    //выбранное значение
    var value by rememberSaveable { mutableStateOf("Select nearby airport") }

    SubHeader(
        text = stringResource(R.string.look_near_you),
        modifier = Modifier.fillMaxWidth()
            .padding(bottom = 8.dp)
    )
    //контейнер для меню
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        //нередактируемое текстовое поле
        TextField(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp)
                .menuAnchor(
                    type = MenuAnchorType.PrimaryNotEditable,
                    enabled = true
                ),
            value = value,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            onValueChange = {
                value = it
            },
            readOnly = true
        )

        //само выпадающее меню
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            //если есть список аэропортов
            if (!nearbyAirports.isNullOrEmpty()) {
                val context = LocalContext.current
                nearbyAirports.forEach { airport ->
                    AirportItem(
                        airport = airport,
                        onSelected = {
                            //если установка завершилась неудачно (т.е. в другом поле уже выбран этот аэропорт
                            if (!setAirport(airport)) {
                                //выводим соответствующий тост
                                when (airportType) {
                                    DestinationType.DEPARTURE -> Toast.makeText(
                                        context,
                                        context.getString(R.string.departure_repeat),
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    DestinationType.ARRIVAL -> Toast.makeText(
                                        context,
                                        context.getString(R.string.arrival_repeat),
                                        Toast.LENGTH_SHORT
                                    ).show()

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
            } else {
                //если еще грузим, то отображаем индикатор
                if (nearbyLoading != true) {
                    viewModel.getNearbyAirports()
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                //иначе если мы ничего не нашли, выводим текст
                else if (nearbyAirports != null && nearbyAirports.isEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = stringResource(R.string.empty_search_result))
                    }
                }
            }
        }
    }
}