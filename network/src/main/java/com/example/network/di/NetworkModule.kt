package com.example.network.di

import com.example.network.api.AirportsAPI
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://aviation-edge.com/v2/public/"

//создание экземпляра Retrofit
fun provideRetrofit(baseUrl: String): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

//создание API
fun provideAirportsAPI(retrofit: Retrofit): AirportsAPI {
    return retrofit.create(AirportsAPI::class.java)
}

//сетевой модуль
val networkModule = module {
    //базовый URL
    single { BASE_URL }

    //retrofit
    single { provideRetrofit(baseUrl = get()) }

    //API
    single { provideAirportsAPI(retrofit = get()) }

}