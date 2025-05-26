package com.example.network.api

import com.example.network.BuildConfig
import com.example.network.models.ResponseNearbyAirport
import retrofit2.http.GET
import retrofit2.http.Query

interface NearbyAPI {
    @GET("nearby")
    suspend fun getNearbyAirports(
        @Query("lat") latitude: Double,
        @Query("lng") longitude: Double,
        @Query("distance") distance: Double = 100.0,
        @Query("key") apiKey: String = BuildConfig.API_KEY
    ): List<ResponseNearbyAirport>
}