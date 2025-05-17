package com.example.search_flights.di

import com.example.search_flights.FlightsSearchRepository
import org.koin.dsl.module

//модуль поиска рейсов
val searchFlightsModule = module {
    //репозиторий
    single { FlightsSearchRepository(api = get()) }
}