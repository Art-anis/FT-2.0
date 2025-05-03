package com.example.search_airports.di

import com.example.search_airports.AirportSearchRepository
import com.example.search_airports.AirportsSearchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

//модуль поиска аэропортов
val searchAirportsModule = module {
    //репозиторий
    single { AirportSearchRepository(airportDao = get(), cityDao = get()) }

    //viewmodel
    viewModel<AirportsSearchViewModel> {
        AirportsSearchViewModel(repository = get())
    }
}