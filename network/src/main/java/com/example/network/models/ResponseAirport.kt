package com.example.network.models

import com.google.gson.annotations.SerializedName

//ответ с API аэропортов
data class ResponseAirport(
    @SerializedName("GMT") var gmt: String? = null, //разница во времени с Гринвичем
    @SerializedName("airportId") var airportId: Int? = null, //id
    @SerializedName("codeIataAirport") var codeIataAirport: String? = null, //код iata аэропорта
    @SerializedName("codeIataCity") var codeIataCity: String? = null, //код iata города
    @SerializedName("codeIcaoAirport") var codeIcaoAirport: String? = null, //код icao аэропорта
    @SerializedName("codeIso2Country") var codeIso2Country: String? = null, //код iso2 страны
    @SerializedName("geonameId") var geonameId: String? = null, //id объекта в geoname
    @SerializedName("latitudeAirport") var latitudeAirport: Double? = null, //ширина
    @SerializedName("longitudeAirport") var longitudeAirport: Double? = null, //долгота
    @SerializedName("nameAirport") var nameAirport: String? = null, //название аэропорта
    @SerializedName("nameCountry") var nameCountry: String? = null, //название страны
    @SerializedName("phone") var phone: String? = null, //телефон аэропорта
    @SerializedName("timezone") var timezone: String? = null //часовой пояс
)