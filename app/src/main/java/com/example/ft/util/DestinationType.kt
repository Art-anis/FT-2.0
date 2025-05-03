package com.example.ft.util

import kotlinx.serialization.Serializable

//тип пункта назначения
@Serializable
enum class DestinationType(val value: String) {
    DEPARTURE("departure"), //пункт вылета
    ARRIVAL("arrival") //пункт прибытия
}