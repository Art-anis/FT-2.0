package com.example.view_flight.di

import com.example.view_flight.ViewFlightViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewFlightModule = module {
    viewModel { ViewFlightViewModel(flightsSearchRepository = get()) }
}