package com.example.ft.navigation

import com.example.ft.util.DestinationType
import com.example.search_airports.util.AirportUIModel
import kotlinx.serialization.Serializable

//вершины навигационного графа

//эагрузочный экран (только для первого запуска приложения)
@Serializable
data object Loading

//маршрут для вложенного графа поиска
@Serializable
data object Search

//поиск рейсов
@Serializable
data class FlightSearch(
    val departure: String = "",
    val arrival: String = ""
)

//поиск аэропорта
@Serializable
data class AirportSearch(
    val type: DestinationType
)

//просмотр результатов поиска
@Serializable
data class FlightListSearchData(
    val departure: AirportUIModel,
    val arrival: AirportUIModel,
    val date: Long
)

@Serializable
//класс рейса
data class FlightData(
    val flightNumber: String, //номер рейса
    val date: Long,
    val departure: String, //название города вылета
    val arrival: String, //название города прибытия
    val tracked: Boolean = false
)

//окно выбора аэропорта для просмотра его расписания
@Serializable
data object AirportTimetable

//просмотр расписания аэропорта
@Serializable
data class AirportFlightList(
    val airport: String,
    val date: Long
)

//просмотр отслеживаемых рейсов
@Serializable
data object TrackedFlights

//авторизация
@Serializable
data object Auth