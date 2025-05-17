package com.example.search_airports.di

import com.example.search_airports.AirportSearchRepository
import org.koin.dsl.module

//модуль поиска аэропортов
val searchAirportsModule = module {
    //репозиторий
    single { AirportSearchRepository(airportDao = get(), cityDao = get()) }
}