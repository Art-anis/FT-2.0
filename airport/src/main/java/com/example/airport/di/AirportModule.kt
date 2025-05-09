package com.example.airport.di

import com.example.airport.AirportRepository
import org.koin.dsl.module

//модуль аэропорта
val airportModule = module {
    //репозиторий
    single { AirportRepository(dao = get()) }
}