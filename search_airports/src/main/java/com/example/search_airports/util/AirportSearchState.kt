package com.example.search_airports.util

//состояние поиска аэропортов
data class AirportSearchState(
    val searchHistory: List<AirportUIModel> = listOf(), //история поиска
    val searchResult: List<AirportUIModel> = listOf(), //результаты поиска
    var loading: Boolean = false //флаг загрузки
)
