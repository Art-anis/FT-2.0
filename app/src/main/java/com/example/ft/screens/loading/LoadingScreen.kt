package com.example.ft.screens.loading

import android.preference.PreferenceManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ft.R
import com.example.loading.LoadingRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

//экран загрузки начальных данных
@Composable
fun LoadingScreen(
    onNavigateToMain: () -> Unit
) {

    //состояние прогресса загрузки
    var progress by rememberSaveable { mutableFloatStateOf(0f) }
    //репозиторий
    val repository = koinInject<LoadingRepository>()

    //контекст
    val context = LocalContext.current
    //SharedPref
    val pref = PreferenceManager.getDefaultSharedPreferences(context)

    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        //запускаем загрузку аэропортов
        scope.launch {
            repository.loadAirports(context)
        }

        //в отдельной корутине запускаем загрузку городов
        scope.launch {
            repository.loadCities(context)
        }

        //общие количества аэропортов и городов
        var totalAirports = 0
        var totalCities = 0

        //пока не загрузили все
        while (progress != 1f) {
            //делаем задержки
            delay(100)

            //обновляем общие количества аэропортов и городов, если они еще не были подгружены
            if (totalAirports == 0) {
                totalAirports = pref.getInt(context.getString(R.string.airports_total_pref_key), 0)
            }
            if (totalCities == 0) {
                totalCities = pref.getInt(context.getString(R.string.cities_total_pref_key), 0)
            }
            //обновляем количество загруженных аэропортов и городов
            val loadedAirports = pref.getInt(context.getString(R.string.airports_loaded_pref_key), 0)
            val loadedCities = pref.getInt(context.getString(R.string.cities_loaded_pref_key), 0)

            //обновляем прогресс
            progress = if (totalAirports != 0 || totalCities != 0) (loadedAirports + loadedCities).toFloat() / (totalAirports + totalCities) else 0f
        }
    }

    //данные о загрузке
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        //если загрузка не завершена, то отображаем индикатор
        if (progress != 1f) {
            //текст, уведомляющий пользователя о загрузке данных
            Text(
                text = stringResource(R.string.loading_notification),
                modifier = Modifier.padding(horizontal = 16.dp),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            //индикатор загрузки
            LoadingAnimation(modifier = Modifier.size(100.dp))
            Spacer(modifier = Modifier.height(16.dp))
            //текст прогресса
            Text(text = "%.2f".format(progress * 100) + "%",)
        }
        //загрузка готова, пользователь может идти дальше
        else {
            Text(text = stringResource(R.string.loading_done))
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onNavigateToMain) {
                Text(text = stringResource(R.string.loaded_btn))
            }
        }
    }
}