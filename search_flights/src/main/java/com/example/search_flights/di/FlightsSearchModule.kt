package com.example.search_flights.di

import com.example.search_flights.FlightsSearchRepository
import com.example.search_flights.FlightsSearchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

//модуль поиска рейсов
val searchFlightsModule = module {
    single { FlightsSearchRepository() }

    viewModel<FlightsSearchViewModel> { FlightsSearchViewModel(airportSearchRepository = get(), flightsSearchRepository = get()) }
}