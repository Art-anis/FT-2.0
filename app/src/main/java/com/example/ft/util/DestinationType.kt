package com.example.ft.util

import kotlinx.serialization.Serializable

@Serializable
enum class DestinationType(val value: String) {
    DEPARTURE("departure"),
    ARRIVAL("arrival")
}