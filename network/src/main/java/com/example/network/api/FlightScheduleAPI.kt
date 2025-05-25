package com.example.network.api

import com.example.network.BuildConfig
import com.example.network.models.ResponseFlightSchedule
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

//api для текущих рейосв
interface FlightScheduleAPI {
    @GET("timetable")
    suspend fun getScheduledFlight(
        @Query("iataCode") airportIata: String, //код аэропорта
        @Query("type") type: String, //тип (вылет/прибытие)
        @Query("airline_iata") airlineIata: String, //iata авиалинии
        @Query("flight_num") flightNumber: String, //номер рейса
        @Query("key") apiKey: String = BuildConfig.API_KEY,
    ): Response<Array<ResponseFlightSchedule>>
}