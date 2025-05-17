package com.example.ft.tracked_flights

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ft.R
import com.example.ft.flight_list.FlightCard
import com.example.ft.navigation.FlightData
import com.example.ft.util.toFlightItemUIModel
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

//просмотр отслеживаемых рейсов
@Composable
fun TrackedFlightsScreen(
    onNavigateToViewFlight: (FlightData) -> Unit
) {

    val viewModel = koinViewModel<TrackedFlightsViewModel>()

    val flightList by viewModel.trackedFlightList.observeAsState()
    val loading by viewModel.loading.observeAsState()

    LaunchedEffect(Unit) {
        if (flightList.isNullOrEmpty()) {
            viewModel.getFlightList()
        }
    }

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
    else {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(flightList ?: emptyList(), key = { item -> "${item.flightNumber} - ${item.departure.time}"}) { flight ->
                SwipeToDeleteContainer(
                    item = flight.toFlightItemUIModel(),
                    onDelete = { },
                    departureCity = flight.departure.cityName,
                    arrivalCity = flight.arrival.cityName,
                    date = flight.departure.time,
                    onNavigateToViewFlight = onNavigateToViewFlight,
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
    val state = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                isRemoved = true
                true
            }
            else false
        }
    )

    LaunchedEffect(isRemoved) {
        if (isRemoved) {
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

@Composable
fun DeleteBackground(
    swipeDismiss: SwipeToDismissBoxState
) {
    val color = if (swipeDismiss.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
        Color.Red
    } else Color.Transparent

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