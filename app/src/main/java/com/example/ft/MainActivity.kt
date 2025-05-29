package com.example.ft

import android.Manifest
import android.app.AlarmManager
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.edit
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.ft.airport_timetable.AirportScreen
import com.example.ft.airport_timetable.AirportTimetableViewModel
import com.example.ft.flight_list.FlightListScreen
import com.example.ft.loading.LoadingScreen
import com.example.ft.navigation.AirportSearch
import com.example.ft.navigation.AirportTimetable
import com.example.ft.navigation.AirportTimetableSearch
import com.example.ft.navigation.Auth
import com.example.ft.navigation.CustomNavType
import com.example.ft.navigation.DrawerBody
import com.example.ft.navigation.DrawerHeader
import com.example.ft.navigation.FlightData
import com.example.ft.navigation.FlightListSearchData
import com.example.ft.navigation.FlightSearch
import com.example.ft.navigation.Loading
import com.example.ft.navigation.MenuItem
import com.example.ft.navigation.NavigationHost
import com.example.ft.navigation.Search
import com.example.ft.navigation.TrackedFlights
import com.example.ft.notifications.scheduleService
import com.example.ft.search.search_airports.AirportSearchScreen
import com.example.ft.search.search_flights.FlightSearchScreen
import com.example.ft.search.search_flights.FlightsSearchViewModel
import com.example.ft.tracked_flights.TrackedFlightsScreen
import com.example.ft.ui.theme.FTTheme
import com.example.ft.users.AuthScreen
import com.example.ft.util.DestinationType
import com.example.ft.util.sharedViewModel
import com.example.ft.view_flight.ViewFlightScreen
import com.example.search_airports.util.AirportUIModel
import com.example.tracked_flights.TrackedFlightsRepository
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import kotlin.reflect.typeOf

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //проверяем, первый ли это запуск приложения, с помощью SharedPref
        val pref = PreferenceManager.getDefaultSharedPreferences(baseContext)
        val firstLaunch = pref.getBoolean(baseContext.getString(R.string.first_time_launch_key), true)

        //если первый запуск
        if (firstLaunch) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                //запрашиваем разрешения
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.POST_NOTIFICATIONS,
                        Manifest.permission.USE_EXACT_ALARM,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ), 89
                )
            }
            val alarmManager = App.getInstance().getSystemService(ALARM_SERVICE) as AlarmManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                //запрашиваем разрешения
                if (!alarmManager.canScheduleExactAlarms()) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.SCHEDULE_EXACT_ALARM), 90
                    )
                }
            }
        }

        enableEdgeToEdge()
        setContent {
            FTTheme {

                //название topBar
                var topAppBarTitle by rememberSaveable { mutableStateOf( baseContext.getString(R.string.top_bar_loading)) }

                //состояние боковой панели
                val drawerState = rememberDrawerState(DrawerValue.Closed)
                val scope = rememberCoroutineScope()

                //создаем навигационный контроллер
                val navController = rememberNavController()
                val currentEntry by navController.currentBackStackEntryAsState()

                //навигационный ящик
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        //выводим ящик, если мы не в окне авторизации
                        val route = currentEntry?.destination?.route
                        if (route?.contains(Auth::class.java.name) != true && route?.contains(Loading::class.java.name) != true) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(0.6f)
                                    .fillMaxHeight()
                                    .background(Color.White)
                            ) {
                                //заголовок
                                DrawerHeader()
                                //тело
                                DrawerBody(
                                    selected = if(currentEntry?.destination?.route?.contains(FlightSearch::class.java.name) == true) {
                                        stringResource(R.string.search_flights_drawer_id)
                                    }
                                    else if (currentEntry?.destination?.route?.contains(AirportTimetableSearch::class.java.name) == true) {
                                        stringResource(R.string.airport_timetable_drawer_id)
                                    }
                                    else if (currentEntry?.destination?.route?.contains(TrackedFlights::class.java.name) == true) {
                                        stringResource(R.string.tracked_flights_drawer_id)
                                    }
                                    else {
                                        ""
                                    },
                                    items = listOf(
                                        MenuItem(
                                            id = stringResource(R.string.search_flights_drawer_id),
                                            title = stringResource(R.string.search_flights_drawer_value),
                                            icon = Icons.Filled.Search
                                        ),
                                        MenuItem(
                                            id = stringResource(R.string.tracked_flights_drawer_id),
                                            title = stringResource(R.string.view_tracked_flights_drawer_value),
                                            icon = Icons.Default.Star
                                        ),
                                        MenuItem(
                                            id = stringResource(R.string.airport_timetable_drawer_id),
                                            title = stringResource(R.string.airport_timetable_drawer_title),
                                            icon = Icons.Default.FlightTakeoff
                                        )
                                    ),
                                    onItemClick = { item ->
                                        //сначала закрываем ящик, потом переходим
                                        scope.launch {
                                            drawerState.close()
                                            when (item.id) {
                                                baseContext.getString(R.string.search_flights_drawer_id) -> {
                                                    if (currentEntry?.destination?.route?.contains(
                                                            FlightSearch::class.java.name
                                                        ) != true
                                                    ) {
                                                        navController.navigate(Search)
                                                    }
                                                }

                                                baseContext.getString(R.string.tracked_flights_drawer_id) -> {
                                                    if (currentEntry?.destination?.route?.contains(
                                                            TrackedFlights.javaClass.name
                                                        ) != true
                                                    ) {
                                                        navController.navigate(TrackedFlights)
                                                    }
                                                }

                                                "Airport timetable" -> {
                                                    if (currentEntry?.destination?.route?.contains(
                                                            AirportTimetableSearch.javaClass.name
                                                        ) != true
                                                    ) {
                                                        navController.navigate(AirportTimetable)
                                                    }
                                                }
                                            }
                                        }
                                    },
                                    signOut = {
                                        val sharedPref = PreferenceManager.getDefaultSharedPreferences(baseContext)
                                        sharedPref.edit {
                                            remove("activeUser")
                                            apply()
                                        }
                                        navController.navigate(Auth) {
                                            popUpTo(currentEntry?.destination?.route ?: "") {
                                                inclusive = true
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) {
                    //тело
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        topBar = {
                            TopAppBar(
                                modifier = Modifier.shadow(elevation = 5.dp,
                                    spotColor = Color.DarkGray),
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = colorResource(R.color.top_bar_color)
                                ),
                                title = {
                                    Text(topAppBarTitle)
                                },
                                navigationIcon = {
                                    //отображаем иконку, если не в окне авторизации
                                    val route = currentEntry?.destination?.route
                                    if (route?.contains(Auth::class.java.name) != true && route?.contains(Loading::class.java.name) != true) {
                                        IconButton(onClick = {
                                            scope.launch {
                                                drawerState.open()
                                            }
                                        }) {
                                            Icon(
                                                imageVector = Icons.Filled.Menu,
                                                contentDescription = null
                                            )
                                        }
                                    }
                                }
                            )
                        }
                    ) { innerPadding ->

                        //проверяем, пришли ли мы из отмены отслеживаемого рейса
                        val fromCancelledNotification = intent.getBooleanExtra("cancelled", false)
                        if (fromCancelledNotification) {
                            //получаем репозиторий
                            val repository = koinInject<TrackedFlightsRepository>()
                            //извлекаем данные из intent
                            val date = intent.getLongExtra("date", 0L)
                            val flightNumber = intent.getStringExtra("flightNumber") ?: ""
                            val username = intent.getStringExtra("username") ?: ""
                            //удаляем рейс и переходим в экран отслеживаемых рейсов
                            LaunchedEffect(Unit) {
                                repository.deleteTrackedFlight(flightNumber, date, username)
                                navController.navigate(TrackedFlights)
                            }
                        }

                        NavigationHost(
                            navController = navController,
                            innerPadding = innerPadding,
                            intent = intent,
                            firstLaunch = firstLaunch,
                            pref = pref,
                            updateTitle = {
                                topAppBarTitle = it
                            }
                        )

                    }
                }
            }
        }
    }
}