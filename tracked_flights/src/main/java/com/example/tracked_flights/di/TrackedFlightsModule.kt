package com.example.tracked_flights.di

import com.example.tracked_flights.TrackedFlightsRepository
import org.koin.dsl.module

//модуль для отслеживаемых рейсов
val trackedFlightsModule = module {
    single { TrackedFlightsRepository(
        trackedFlightDao = get(),
        airportDao = get(),
        cityDao = get(),
        airlineDao = get()
    ) }
}