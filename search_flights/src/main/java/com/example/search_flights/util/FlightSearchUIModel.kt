package com.example.search_flights.util

import com.example.search_airports.util.AirportUIModel
import java.util.Calendar
import java.util.Date

//UI-модель для поиска рейсов
data class FlightSearchUIModel(
    val departure: AirportUIModel = AirportUIModel(),
    val arrival: AirportUIModel = AirportUIModel(),
    val date: Date = Calendar.getInstance().time
)
