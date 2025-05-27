package com.example.ft.navigation

import android.content.Intent
import android.content.SharedPreferences
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.example.ft.R
import com.example.ft.airport_timetable.AirportScreen
import com.example.ft.airport_timetable.AirportTimetableViewModel
import com.example.ft.flight_list.FlightListScreen
import com.example.ft.loading.LoadingScreen
import com.example.ft.search.search_airports.AirportSearchScreen
import com.example.ft.search.search_flights.FlightSearchScreen
import com.example.ft.search.search_flights.FlightsSearchViewModel
import com.example.ft.tracked_flights.TrackedFlightsScreen
import com.example.ft.users.AuthScreen
import com.example.ft.util.DestinationType
import com.example.ft.util.sharedViewModel
import com.example.ft.view_flight.ViewFlightScreen
import com.example.search_airports.util.AirportUIModel
import kotlin.reflect.typeOf

@Composable
fun NavigationHost(
    navController: NavHostController,
    innerPadding: PaddingValues,
    intent: Intent,
    firstLaunch: Boolean,
    pref: SharedPreferences,
    updateTitle: (String) -> Unit
) {
    //хост навигации
    NavHost(
        navController = navController, //передаем контроллер
        modifier = Modifier.padding(innerPadding), //задаем отступы
        //определяем начальный узел графа в зависимости от того, первый ли это запуск
        startDestination = if (firstLaunch) {
            //если это был первый запуск, устанавливаем флаг в false
            val editor = pref.edit()
            editor
                .putBoolean(LocalContext.current.getString(R.string.first_time_launch_key), false)
                .apply()
            editor.clear()
            //возвращаем загрузочный экран как стартовую вершину
            Loading
        } else {
            //если нет активного пользователя, выводим окно авторизации
            val activeUsername = pref.getString("activeUser", "")
            if (activeUsername.isNullOrEmpty()) {
                Auth
            }
            else if (intent.getSerializableExtra("data") != null){
                intent.getSerializableExtra("data") as FlightData
            }
            else {
                Search
            }
        }
    ) {
        //экран загрузки
        composable<Loading> {
            LoadingScreen(
                onNavigateToMain = {
                    //убираем экран загрузки из стека
                    navController.popBackStack()
                    //переходим на экран авторизации
                    navController.navigate(Auth)
                }
            )
        }

        //вложенный навигационный граф для поиска
        navigation<Search>(
            startDestination = FlightSearch()
        ) {
            //экран поиска рейсов
            composable<FlightSearch> { entry ->
                //обновление заголовка
                updateTitle(stringResource(R.string.top_bar_search_flights))

                //получение общей viewmodel
                val viewModel = entry.sharedViewModel<FlightsSearchViewModel>(navController)
                //сам экран
                FlightSearchScreen(
                    viewModel = viewModel,
                    onNavigateToAirportSearch = { type ->
                        //переходим на экран поиска аэропорта
                        navController.navigate(AirportSearch(type))
                    },
                    onLaunchSearch = { departure, arrival, date ->
                        //переходи на экран просмотра списка рейсов
                        navController.navigate(FlightListSearchData(
                            departure = departure,
                            arrival = arrival,
                            date = date.time
                        ))
                    }
                )
            }

            //экран поиска аэропортов
            composable<AirportSearch> { entry ->
                //обновление заголовка
                updateTitle(stringResource(R.string.top_bar_search_airport))

                //получение общей viewmodel
                val viewModel = entry.sharedViewModel<FlightsSearchViewModel>(navController)
                val type = entry.toRoute<AirportSearch>().type
                //сам экран
                AirportSearchScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    airportType = type!!,
                    setAirport = { airport ->
                        when(type) {
                            DestinationType.DEPARTURE -> viewModel.setDeparture(airport)
                            DestinationType.ARRIVAL -> viewModel.setArrival(airport)
                        }
                    }
                )
            }
        }

        //экран просмотра результатов поиска
        composable<FlightListSearchData>(
            typeMap = mapOf(typeOf<AirportUIModel>() to CustomNavType.AirportNavType)
        ) { entry ->
            //данные для поиска
            val data = entry.toRoute<FlightListSearchData>()

            //обновление заголовка
            updateTitle("${data.departure.cityName} - ${data.arrival.cityName}")

            //сам экран
            FlightListScreen(
                searchData = data,
                departureCityName = data.departure.cityName,
                arrivalCityName = data.arrival.cityName,
                onNavigateToViewFlight = { flightData ->
                    navController.navigate(flightData)
                }
            )
        }

        //экран просмотра данных о рейсе
        composable<FlightData>(
            typeMap = mapOf(typeOf<FlightData>() to CustomNavType.FlightNavType)
        ) { entry ->
            val flightData = entry.toRoute<FlightData>()

            updateTitle(flightData.flightNumber)
            ViewFlightScreen(
                flightData = flightData
            )
        }

        //экран просмотра
        composable<TrackedFlights> {
            updateTitle(stringResource(R.string.tracked_flights_title))

            TrackedFlightsScreen(
                onNavigateToViewFlight = { flightData ->
                    navController.navigate(flightData)
                }
            )
        }

        //граф расписания аэропорта
        navigation<AirportTimetable>(
            startDestination = AirportTimetableSearch
        ) {
            //экран выбора аэропорта для просмотра его расписания
            composable<AirportTimetableSearch> { entry ->
                updateTitle("Airport Timetable")
                val viewModel = entry.sharedViewModel<AirportTimetableViewModel>(navController)
                AirportScreen(
                    onNavigateToAirportSearch = {
                        navController.navigate(AirportSearch(null))
                    },
                    viewModel = viewModel,
                    onNavigateToViewFlight = { flightData ->
                        navController.navigate(flightData)
                    }
                )
            }

            composable<AirportSearch> { entry ->
                updateTitle("Search Airport")
                val viewModel = entry.sharedViewModel<AirportTimetableViewModel>(navController)
                AirportSearchScreen(
                    airportType = null,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    setAirport = { airport ->
                        viewModel.setAirport(airport)
                    }
                )
            }
        }

        //экран авторизации
        composable<Auth> {
            updateTitle("Log in")
            AuthScreen(
                firstLaunch = firstLaunch,
                navigateToSearch = {
                    navController.navigate(Search) {
                        popUpTo(Auth) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}