package com.example.ft.flight_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.flight_list.util.FlightItemUIModel
import com.example.ft.navigation.FlightData
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

//карточка с данными о рейсе
@Composable
fun FlightCard(
    flight: FlightItemUIModel, //рейс
    departureCity: String, //название города вылета
    arrivalCity: String, //название города прибытия
    date: Long, //дата вылета
    onNavigateToViewFlight: (FlightData) -> Unit, //функция перехода на экран просмотра рейса
    showDate: Boolean = false, //отображать дату или нет (для отслеживаемых рейсов)
    fromTimetable: Boolean = false
) {
    //контейнер
    Card(
        modifier = Modifier.fillMaxWidth()
            .padding(
                horizontal = 32.dp,
                vertical = 16.dp
            )
            //при нажатии на карточку переходим на экран просмотра
            .clickable {
                val (departureHours, departureMinutes) = flight.departureTime.split(":").map { it.toInt() }

                //устанавливаем точное время вылета
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = date
                calendar.set(Calendar.HOUR_OF_DAY, departureHours)
                calendar.set(Calendar.MINUTE, departureMinutes)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)

                //переходим на экран просмотра рейса
                onNavigateToViewFlight(
                    FlightData(
                        flightNumber = flight.flightNumber.uppercase(),
                        departure = departureCity,
                        arrival = arrivalCity,
                        date = calendar.timeInMillis,
                        tracked = flight.tracked,
                        fromTimetable = fromTimetable
                    )
                )
            }
    ){
        //текст с номером рейса и датой вылета
        if (flight.flightNumber.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(
                        start = 8.dp,
                        bottom = 16.dp,
                        top = 8.dp
                    )
            ) {
                //отображаем дату, если надо
                if (showDate) {
                    val displayDate = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).format(date)
                    Text(
                        text = displayDate,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
                //номер рейса
                Text(
                    text = flight.flightNumber
                )
            }
        }
        //контейнер для данных об аэропортах
        Box(modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .padding(bottom = 8.dp)
        ) {
            //аэропорт вылета
            FlightCardAirportData(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .fillMaxWidth(0.5f)
                    .padding(start = 8.dp),
                city = departureCity,
                iataCode = flight.departureIata,
                time = flight.departureTime,
                alignment = "start"
            )
            //аэропорт прибытия
            FlightCardAirportData(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxWidth(0.5f)
                    .padding(end = 8.dp),
                city = arrivalCity,
                iataCode = flight.arrivalIata,
                time = flight.arrivalTime,
                alignment = "end"
            )
        }
    }
}