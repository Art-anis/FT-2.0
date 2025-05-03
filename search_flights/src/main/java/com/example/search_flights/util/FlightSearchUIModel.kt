package com.example.search_flights.util

import com.example.search_airports.util.AirportUIModel
import java.util.Calendar
import java.util.Date

//UI-модель для поиска рейсов
data class FlightSearchUIModel(
    var departure: AirportUIModel = AirportUIModel(), //аэропорт прилета
    var arrival: AirportUIModel = AirportUIModel(), //аэропорт прибытия
    var date: Date = Calendar.getInstance().time //дата
)
