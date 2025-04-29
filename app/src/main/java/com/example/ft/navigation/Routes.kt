package com.example.ft.navigation

import kotlinx.serialization.Serializable

//вершины навигационного графа

//эагрузочный экран (только для первого запуска приложения)
@Serializable
data object Loading

//поиск рейсов
@Serializable
data class FlightSearch(
    val departure: String = "",
    val arrival: String = ""
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