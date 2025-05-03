package com.example.search_airports.util

data class AirportSearchState(
    val searchHistory: List<AirportUIModel> = listOf(),
    val searchResult: List<AirportUIModel> = listOf(),
    var loading: Boolean = false
)
