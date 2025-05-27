package com.example.ft.di

import com.example.ft.airport_timetable.AirportTimetableViewModel
import com.example.ft.flight_list.FlightListViewModel
import com.example.ft.search.search_airports.AirportsSearchViewModel
import com.example.ft.search.search_flights.FlightsSearchViewModel
import com.example.ft.tracked_flights.TrackedFlightsViewModel
import com.example.ft.users.AuthViewModel
import com.example.ft.view_flight.ViewFlightViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

//модуль app для di
val appModule = module {
    //все viewmodel
    viewModel { FlightListViewModel(flightSearchRepository = get()) }

    viewModel { AirportsSearchViewModel(repository = get()) }

    viewModel { FlightsSearchViewModel(repository = get()) }

    viewModel { TrackedFlightsViewModel(repository = get()) }

    viewModel { ViewFlightViewModel(
        flightsSearchRepository = get(),
        airportRepository = get(),
        trackedFlightsRepository = get()
    ) }

    viewModel { AuthViewModel(repository = get()) }

    viewModel { AirportTimetableViewModel(repository = get()) }
}