package com.example.ft.view_flight

import android.content.Intent
import android.provider.CalendarContract
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.example.ft.R
import com.example.ft.navigation.FlightData
import com.example.ft.notifications.scheduleReminder
import com.example.ft.view_flight.airline_codeshared.AirlineAndCodesharedRow
import com.example.ft.view_flight.route.RouteRow
import com.example.ft.view_flight.terminal_gate.TermAndGateRow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

//url для получения логотипов авиалиний
const val AIRLINE_LOGO_URL = "https://pics.avs.io/200/50/"

//экран просмотра отдельного рейса
@Composable
fun ViewFlightScreen(
    flightData: FlightData, //данные о рейсе
) {
    //viewmodel
    val viewFlightViewModel = koinViewModel<ViewFlightViewModel>()

    //состояние данных о рейсе
    val flight by viewFlightViewModel.flightData.observeAsState()

    var tracked by rememberSaveable { mutableStateOf(flightData.tracked) }

    //получаем данные из репозитория
    LaunchedEffect(Unit) {
        if (flightData.tracked) {
            viewFlightViewModel.getTrackedFlight(flightData.flightNumber, flightData.date)
        }
        else {
            viewFlightViewModel.getFlight(
                flightNumber = flightData.flightNumber,
                departure = flightData.departure,
                arrival = flightData.arrival
            )
        }

        tracked = viewFlightViewModel.checkIfTracked(
            flightIata = flightData.flightNumber,
            date = flightData.date
        )
    }

    //отображаем данные о рейсе
    flight?.let { data ->
        val scrollState = rememberScrollState()

        //состояние загрузки логотипа основной авиалинии
        var loadingMain by rememberSaveable { mutableStateOf(true) }
        //сам логотип
        val mainAirlineLogo = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(AIRLINE_LOGO_URL + data.airline.airlineIata.uppercase() + ".png")
                .build(),
            onSuccess = {
                loadingMain = false
            }
        )

        //состояние загрузки логотипа кодшеринга
        var loadingCodeShared by rememberSaveable { mutableStateOf(data.codeshared != null) }
        //сам логотип, если нужен
        lateinit var codeSharedLogo: AsyncImagePainter
        data.codeshared?.let {
            codeSharedLogo = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(AIRLINE_LOGO_URL + it.airlineIata.uppercase() + ".png")
                    .build(),
                onSuccess = {
                    loadingCodeShared = false
                }
            )
        }

        //если логотипы еще грузятся, не отображаем данные
        if (loadingMain || loadingCodeShared) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
                Text(stringResource(R.string.loading_flight))
            }
        }
        else {
            //контейнер
            Column {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.9f)
                        .verticalScroll(state = scrollState)
                ) {
                    //места вылета и прибытия
                    RouteRow(
                        departure = data.departure,
                        arrival = data.arrival,
                        status = data.status
                    )
                    //номера терминалов и выходов
                    TermAndGateRow(
                        departureTerminal = data.departure.terminal,
                        departureGate = data.departure.gate,
                        arrivalTerminal = data.arrival.terminal,
                        arrivalGate = data.arrival.gate
                    )
                    //данные об авиалинии и кодшеринге, если он есть
                    AirlineAndCodesharedRow(
                        mainAirline = data.airline,
                        mainAirlineLogo = mainAirlineLogo,
                        codeshared = data.codeshared,
                        codesharedLogo = if (data.codeshared == null) null else codeSharedLogo
                    )
                }
                //кнопка для отслеживания рейса
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val scope = rememberCoroutineScope()
                    val context = LocalContext.current
                    //сама кнопка
                    Button(
                        enabled = !tracked,
                        onClick = {
                            scope.launch {
                                //получаем gmt аэропорта
                                val departureGmt = viewFlightViewModel.getGmt(data.departure.iata)?.toInt()
                                departureGmt?.let {
                                    //получаем локальный gmt
                                    val localGmt = SimpleDateFormat("ZZZ", Locale.ENGLISH).format(System.currentTimeMillis()).substring(0..2).toInt()
                                    //извлекаем часы и минуты из времени вылета
                                    val (hours, minutes) = data.departure.time.split(":").map { it.toInt() }

                                    //создаем календарь
                                    val calendar = Calendar.getInstance()
                                    calendar.timeInMillis = flightData.date

                                    //устанавливаем время
                                    calendar.set(Calendar.HOUR_OF_DAY, hours)
                                    calendar.set(Calendar.MINUTE, minutes)

                                    //переводим его в часовой пояс пользователя
                                    calendar.add(Calendar.HOUR_OF_DAY, -departureGmt)
                                    calendar.add(Calendar.HOUR_OF_DAY, localGmt)

                                    val id = viewFlightViewModel.trackFlight(data, flightData.date)

                                    //intent для календаря
                                    val intent = Intent(Intent.ACTION_INSERT)
                                        .setData(CalendarContract.Events.CONTENT_URI)
                                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calendar.timeInMillis)
                                        .putExtra(CalendarContract.Events.TITLE, "Flight from ${flightData.departure} to ${flightData.arrival}")
                                        .putExtra(CalendarContract.Events.DESCRIPTION, "This flight will depart from ${data.departure.iata.uppercase()}, ${flightData.departure}" +
                                                " and arrive to ${data.arrival.iata.uppercase()}, ${flightData.arrival}")

                                    //обновляем состояние отслеживания
                                    tracked = true

                                    //если прошло успешно, ставим напоминание за неделю до вылета
                                    if (id != -1) {
                                        calendar.add(Calendar.DATE, -7)
                                        scheduleReminder(
                                            calendar = calendar,
                                            id = "${id}7".toInt(),
                                            flightNumber = flight?.flightNumber ?: "",
                                            airlineIata = flight?.airline?.airlineIata ?: "",
                                            airportIata = flight?.departure?.iata ?: ""
                                        )
                                    }

                                    //запускаем календарь
                                    context.startActivity(intent)
                                }
                            }
                        }
                    ) {
                        Text(stringResource(if (!tracked) R.string.track_flight else R.string.already_tracked))
                    }
                }
            }
        }
    }
}