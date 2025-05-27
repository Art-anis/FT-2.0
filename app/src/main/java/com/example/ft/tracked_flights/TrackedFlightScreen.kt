package com.example.ft.tracked_flights

import android.preference.PreferenceManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ft.R
import com.example.ft.flight_list.FlightCard
import com.example.ft.navigation.FlightData
import com.example.ft.util.SubHeader
import com.example.ft.util.toFlightItemUIModel
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

//просмотр отслеживаемых рейсов
@Composable
fun TrackedFlightsScreen(
    onNavigateToViewFlight: (FlightData) -> Unit
) {

    //viewmodel
    val viewModel = koinViewModel<TrackedFlightsViewModel>()

    //список рейсов
    val flightList by viewModel.trackedFlightList.observeAsState()
    //флаг загрузки
    val loading by viewModel.loading.observeAsState()

    //при открытии получаем список рейсов
    LaunchedEffect(Unit) {
        viewModel.getFlightList()
    }

    //если еще грузим, то показываем колесо
    if (loading == true || loading == null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
            Text(stringResource(R.string.fetching_tracked_flights))
        }
    }
    //иначе отображаем список
    else {
        Column {
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(LocalContext.current)
            val username = sharedPref.getString("activeUser", "")

            SubHeader(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 32.dp, bottom = 8.dp),
                text = "Viewing $username's tracked flights"
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(flightList ?: emptyList(), key = { item -> "${item.flightNumber} - ${item.departure.time}"}) { flight ->
                    //контейнер
                    SwipeToDeleteContainer(
                        item = flight.toFlightItemUIModel(),
                        //при свайпе удаляем запись
                        onDelete = {
                            viewModel.onDelete(flight.flightNumber, flight.departure.time)
                        },
                        departureCity = flight.departure.cityName,
                        arrivalCity = flight.arrival.cityName,
                        date = flight.departure.time,
                        onNavigateToViewFlight = onNavigateToViewFlight,
                        //содержимое
                        content = { item, depCity, arrCity, date, nav ->
                            FlightCard(
                                flight = item,
                                departureCity = depCity,
                                arrivalCity = arrCity,
                                date = date,
                                showDate = true,
                                onNavigateToViewFlight = nav
                            )
                        },
                    )
                }
            }
        }
    }
}

//контейнер для отслеживаемого рейса
@Composable
fun <T, R> SwipeToDeleteContainer(
    item: T,
    onDelete: (T) -> Unit,
    animationDuration: Int = 500,
    departureCity: String,
    arrivalCity: String,
    date: Long,
    onNavigateToViewFlight: (R) -> Unit,
    content: @Composable (T, String, String, Long, (R) -> Unit) -> Unit
) {
    var isRemoved by remember { mutableStateOf(false) }
    //состояние
    val state = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                isRemoved = true
                true
            }
            else false
        }
    )

    //запускаем эффект, если изменяется состояние isRemoved
    LaunchedEffect(isRemoved) {
        if (isRemoved) {
            //отыгрываем анимацию, потом удаляем рейс
            delay(animationDuration.toLong())
            onDelete(item)
        }
    }

    AnimatedVisibility(
        visible = !isRemoved,
        exit = shrinkVertically(
            animationSpec = tween(animationDuration),
            shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        //само содержимое
        SwipeToDismissBox(
            state = state,
            backgroundContent = {
                DeleteBackground(state)
            },
            content = { content(item, departureCity, arrivalCity, date, onNavigateToViewFlight) },
            enableDismissFromEndToStart = true
        )
    }
}

//фон для удаления
@Composable
fun DeleteBackground(
    swipeDismiss: SwipeToDismissBoxState
) {
    //цвет фона
    val color = if (swipeDismiss.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
        Color.Red
    } else Color.Transparent

    //иконка с корзиной
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color)
        .padding(16.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = null,
            tint = Color.White
        )
    }
}