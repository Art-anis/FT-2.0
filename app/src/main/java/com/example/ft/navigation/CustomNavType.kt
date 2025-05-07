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
    val FlightNavType = object: NavType<FlightData>(isNullableAllowed = false) {
        override fun get(
            bundle: Bundle,
            key: String
        ): FlightData? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun serializeAsValue(value: FlightData): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun parseValue(value: String): FlightData {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun put(
            bundle: Bundle,
            key: String,
            value: FlightData
        ) {
            bundle.putString(key, Json.encodeToString(value))
        }
    }

    //аэропорты
    val AirportNavType = object: NavType<AirportUIModel>(isNullableAllowed = false) {
        override fun get(
            bundle: Bundle,
            key: String
        ): AirportUIModel? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun serializeAsValue(value: AirportUIModel): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun parseValue(value: String): AirportUIModel {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun put(
            bundle: Bundle,
            key: String,
            value: AirportUIModel
        ) {
            bundle.putString(key, Json.encodeToString(value))
        }

    }
}