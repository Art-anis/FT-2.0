package com.example.ft

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.ft.airport_timetable.AirportScreen
import com.example.ft.airport_timetable.AirportTimetableScreen
import com.example.ft.flight_list.FlightListScreen
import com.example.ft.loading.LoadingScreen
import com.example.ft.navigation.AirportFlightList
import com.example.ft.navigation.AirportSearch
import com.example.ft.navigation.AirportTimetable
import com.example.ft.navigation.CustomNavType
import com.example.ft.navigation.FlightData
import com.example.ft.navigation.FlightListSearchData
import com.example.ft.navigation.FlightSearch
import com.example.ft.navigation.Loading
import com.example.ft.navigation.Search
import com.example.ft.navigation.TrackedFlights
import com.example.ft.search.search_airports.AirportSearchScreen
import com.example.ft.search.search_flights.FlightSearchScreen
import com.example.ft.tracked_flights.TrackedFlightsScreen
import com.example.ft.ui.theme.FTTheme
import com.example.ft.util.DestinationType
import com.example.ft.util.sharedViewModel
import com.example.ft.view_flight.ViewFlightScreen
import com.example.search_airports.util.AirportUIModel
import com.example.search_flights.FlightsSearchViewModel
import kotlin.reflect.typeOf

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //проверяем, первый ли это запуск приложения, с помощью SharedPref
        val pref = PreferenceManager.getDefaultSharedPreferences(baseContext)
        val firstLaunch = pref.getBoolean(baseContext.getString(R.string.first_time_launch_key), true)

        enableEdgeToEdge()
        setContent {
            FTTheme {

                //название topBar
                var topAppBarTitle by rememberSaveable { mutableStateOf( baseContext.getString(R.string.top_bar_loading)) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(topAppBarTitle)
                            }
                        )
                    }
                ) { innerPadding ->
                    //создаем навигационный контроллер
                    val navController = rememberNavController()
                    //хост навигации
                    NavHost(
                        navController = navController, //передаем контроллер
                        modifier = Modifier.padding(innerPadding), //задаем отступы
                        //определяем начальный узел графа в зависимости от того, первый ли это запуск
                        startDestination = if (firstLaunch) {
                            //если это был первый запуск, устанавливаем флаг в false
                            val editor = pref.edit()
                            editor
                                .putBoolean(baseContext.getString(R.string.first_time_launch_key), false)
                                .apply()
                            editor.clear()
                            //возвращаем загрузочный экран как стартовую вершину
                            Loading
                        } else Search
                    ) {
                        //экран загрузки
                        composable<Loading> {
                            LoadingScreen(
                                onNavigateToMain = {
                                    //убираем экран загрузки из стека
                                    navController.popBackStack()
                                    //переходим на экран поиска
                                    navController.navigate(Search)
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
                                topAppBarTitle = stringResource(R.string.top_bar_search_flights)

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
                                topAppBarTitle = stringResource(R.string.top_bar_search_airport)

                                //получение общей viewmodel
                                val viewModel = entry.sharedViewModel<FlightsSearchViewModel>(navController)
                                val type = entry.toRoute<AirportSearch>().type
                                //сам экран
                                AirportSearchScreen(
                                    onNavigateBack = {
                                        navController.popBackStack()
                                    },
                                    airportType = type,
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
                            topAppBarTitle = "${data.departure.cityName} - ${data.arrival.cityName}"

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

                            topAppBarTitle = flightData.flightNumber
                            ViewFlightScreen(
                                flightData = flightData
                            )
                        }

                        //экран просмотра
                        composable<TrackedFlights> {
                            TrackedFlightsScreen()
                        }

                        //экран расписания аэропорта
                        composable<AirportFlightList> {
                            AirportTimetableScreen()
                        }

                        //экран выбора аэропорта для просмотра его расписания
                        composable<AirportTimetable> {
                            AirportScreen()
                        }
                    }
                }
            }
        }
    }
}