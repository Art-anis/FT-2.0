package com.example.ft

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ft.navigation.AirportFlightList
import com.example.ft.navigation.AirportTimetable
import com.example.ft.navigation.CustomNavType
import com.example.ft.navigation.Flight
import com.example.ft.navigation.FlightData
import com.example.ft.navigation.FlightList
import com.example.ft.navigation.FlightSearch
import com.example.ft.navigation.Loading
import com.example.ft.navigation.TrackedFlights
import com.example.ft.screens.AirportScreen
import com.example.ft.screens.AirportTimetableScreen
import com.example.ft.screens.FlightListScreen
import com.example.ft.screens.FlightSearchScreen
import com.example.ft.screens.loading.LoadingScreen
import com.example.ft.screens.TrackedFlightsScreen
import com.example.ft.screens.ViewFlightScreen
import com.example.ft.ui.theme.FTTheme
import kotlin.reflect.typeOf

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //проверяем, первый ли это запуск приложения, с помощью SharedPref
        val pref = PreferenceManager.getDefaultSharedPreferences(baseContext)
        val firstLaunch = pref.getBoolean(baseContext.getString(R.string.first_time_launch_key), true)

        enableEdgeToEdge()
        setContent {
            FTTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
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
                            Loading
                        } else FlightSearch()
                    ) {
                        //экран загрузки
                        composable<Loading> {
                            LoadingScreen(
                                onNavigateToMain = {
                                    //убираем экран загрузки из стека
                                    navController.popBackStack()
                                    //переходим на экран поиска
                                    navController.navigate(FlightSearch())
                                }
                            )
                        }

                        //экран поиска рейсов
                        composable<FlightSearch> {
                            FlightSearchScreen()
                        }

                        //экран просмотра результатов поиска
                        composable<FlightList> {
                            FlightListScreen()
                        }

                        //экран просмотра данных о рейсе
                        composable<FlightData>(
                            typeMap = mapOf(typeOf<Flight>() to CustomNavType.FlightNavType)
                        ) {
                            ViewFlightScreen()
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