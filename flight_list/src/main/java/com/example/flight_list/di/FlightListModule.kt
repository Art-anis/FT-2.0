package com.example.flight_list.di

import com.example.flight_list.FlightListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val flightListModule = module {
    viewModel { FlightListViewModel(flightSearchRepository = get()) }
}