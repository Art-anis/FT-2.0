package com.example.network.api

import com.example.network.BuildConfig
import com.example.network.models.ResponseFutureFlight
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

//api для рейсов позднее, чем через неделю
interface FutureFlightsAPI {

    //получение списка рейсов
    @GET("flightsFuture")
    suspend fun getFlights(
        @Query("key") apiKey: String = BuildConfig.API_KEY, //ключ
        @Query("iataCode") airportIata: String, //код iata аэропорта вылета/прибытия
        @Query("type") destinationType: String, //тип аэропорта - аэропорт вылета/прибытия
        @Query("date") date: String //дата вылета/прибытия
    ): Response<Array<ResponseFutureFlight>>
}