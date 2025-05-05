package com.example.search_flights.di

import com.example.search_flights.FlightsSearchRepository
import com.example.search_flights.FlightsSearchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

//модуль поиска рейсов
val searchFlightsModule = module {
    //репозиторий
    single { FlightsSearchRepository(api = get()) }

    //viewmodel
    viewModel<FlightsSearchViewModel> { FlightsSearchViewModel(repository = get()) }
}