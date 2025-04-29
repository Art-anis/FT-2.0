package com.example.network.api

import com.example.network.BuildConfig
import com.example.network.models.ResponseAirport
import retrofit2.http.GET
import retrofit2.http.Query

//API аэропортов
interface AirportsAPI {

    @GET("airportDatabase")
    suspend fun getAllAirports(
        @Query("key") apiKey: String = BuildConfig.API_KEY
    ): List<ResponseAirport>
}