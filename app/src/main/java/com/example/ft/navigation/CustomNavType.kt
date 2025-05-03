package com.example.ft.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.example.search_airports.util.AirportUIModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

//набор кастомных NavType для графа навигации
object CustomNavType {
    //рейсы
    val FlightNavType = object: NavType<Flight>(isNullableAllowed = false) {
        override fun get(
            bundle: Bundle,
            key: String
        ): Flight? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun serializeAsValue(value: Flight): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun parseValue(value: String): Flight {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun put(
            bundle: Bundle,
            key: String,
            value: Flight
        ) {
            bundle.putString(key, Json.encodeToString(value))
        }
    }
}