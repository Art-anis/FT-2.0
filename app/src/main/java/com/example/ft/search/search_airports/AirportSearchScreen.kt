package com.example.ft.search.search_airports

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ft.R
import com.example.ft.util.DestinationType
import com.example.ft.util.SubHeader
import com.example.search_airports.util.AirportUIModel
import org.koin.androidx.compose.koinViewModel

//экран поиска аэропортов
@SuppressLint("MissingPermission")
@Composable
fun AirportSearchScreen(
    airportType: DestinationType?, //вариант назначения
    onNavigateBack: () -> Unit, //обратная навигация
    setAirport: (AirportUIModel) -> Boolean //функция выбора аэропорта в зависимости от типа
) {

    val viewModel = koinViewModel<AirportsSearchViewModel>()
    //список аэропортов в поиске
    val airports by viewModel.airportSearchState.observeAsState()

    val nearbyAirports by viewModel.nearbyAirports.observeAsState()
    val nearbyLoading by viewModel.nearbyLoading.observeAsState()

    //состояние значения поиска
    val query = rememberTextFieldState()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        //поисковая строка
        AirportSearchBar(
            query = query,
            searchAirports = viewModel::searchAirport,
            clearSearch = viewModel::clearSearch,
            onNavigateBack = onNavigateBack
        )

        //контейнер
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            //история поиска
            airports?.searchHistory?.let {
                item {
                    AirportSearchHistory(
                        history = it,
                        airportType = airportType,
                        onNavigateBack = onNavigateBack,
                        viewModel = viewModel,
                        setAirport = setAirport
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            //выбор аэропорта рядом
            item {
                NearbyAirportMenu(
                    nearbyAirports = nearbyAirports,
                    nearbyLoading = nearbyLoading,
                    airportType = airportType,
                    setAirport = setAirport,
                    viewModel = viewModel,
                    onNavigateBack = onNavigateBack
                )
            }


            item {
                SubHeader(
                    text = stringResource(R.string.search_results_header),
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 16.dp, bottom = 16.dp)
                )
            }

            airports?.searchResult?.let {
                airportSearchResults(
                    searchResult = it,
                    loading = airports?.loading == true,
                    query = query,
                    airportType = airportType,
                    onNavigateBack = onNavigateBack,
                    viewModel = viewModel,
                    setAirport = setAirport
                )
            }

        }
    }

    //при переходе назад очищаем результаты поиска
    BackHandler {
        viewModel.clearSearch()
        onNavigateBack()
    }
}