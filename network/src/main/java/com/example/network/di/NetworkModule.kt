package com.example.network.di

import com.example.network.api.AirlinesAPI
import com.example.network.api.AirportsAPI
import com.example.network.api.FlightScheduleAPI
import com.example.network.api.FutureFlightsAPI
import com.example.network.api.NearbyAPI
import com.example.network.models.ResponseFlightSchedule
import com.example.network.models.ResponseFutureFlight
import com.example.network.util.FlightDeserializer
import com.example.network.util.FlightScheduleDeserializer
import com.google.gson.GsonBuilder
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://aviation-edge.com/v2/public/"

//создание экземпляра Retrofit
fun provideRetrofit(baseUrl: String): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder()
                    .registerTypeAdapter(Array<ResponseFutureFlight>::class.java, FlightDeserializer())
                    .registerTypeAdapter(Array<ResponseFlightSchedule>::class.java, FlightScheduleDeserializer())
                    .create()
            )
        )
        .build()
}

//создание API
fun provideAirportsAPI(retrofit: Retrofit): AirportsAPI {
    return retrofit.create(AirportsAPI::class.java)
}

fun provideFlightsAPI(retrofit: Retrofit): FutureFlightsAPI {
    return retrofit.create(FutureFlightsAPI::class.java)
}

fun provideAirlinesAPI(retrofit: Retrofit): AirlinesAPI {
    return retrofit.create(AirlinesAPI::class.java)
}

fun provideFlightScheduleAPI(retrofit: Retrofit): FlightScheduleAPI {
    return retrofit.create(FlightScheduleAPI::class.java)
}

fun provideNearbyApi(retrofit: Retrofit): NearbyAPI {
    return retrofit.create(NearbyAPI::class.java)
}

//сетевой модуль
val networkModule = module {
    //базовый URL
    single { BASE_URL }

    //retrofit
    single { provideRetrofit(baseUrl = get()) }

    //API
    single { provideAirportsAPI(retrofit = get()) }
    single { provideFlightsAPI(retrofit = get()) }
    single { provideAirlinesAPI(retrofit = get()) }
    single { provideFlightScheduleAPI(retrofit = get()) }
    single { provideNearbyApi(retrofit = get()) }

}