package com.example.ft.navigation

import com.example.ft.util.DestinationType
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
data class FlightList(
    val departure: String,
    val arrival: String,
    val date: Long
)
//просмотр конкретного рейса
@Serializable
data class FlightData(
    val flight: Flight
)

@Serializable
//класс рейса
data class Flight(
    val id: Int
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