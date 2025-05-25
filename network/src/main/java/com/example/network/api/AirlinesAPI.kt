package com.example.network.api

import com.example.network.BuildConfig
import com.example.network.models.ResponseAirline
import retrofit2.http.GET
import retrofit2.http.Query

//api для авиалиний
interface AirlinesAPI {

    //получение всех авиалиний
    @GET("airlineDatabase")
    suspend fun getAllAirlines(
        @Query("key") apiKey: String = BuildConfig.API_KEY
    ): List<ResponseAirline>
}