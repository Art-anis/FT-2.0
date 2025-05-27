package com.example.ft.update_tracked_data

import android.preference.PreferenceManager
import android.text.Html
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRightAlt
import androidx.compose.material.icons.filled.ArrowRightAlt
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.example.db.entities.TrackedFlightEntity
import com.example.ft.App
import com.example.ft.R
import com.example.ft.util.SubHeader
import com.example.ft.util.TrackedFlightUpdateData
import com.example.ft.view_flight.AIRLINE_LOGO_URL
import com.example.tracked_flights.TrackedFlightsRepository
import org.koin.compose.koinInject
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.reflect.full.memberProperties
import kotlin.reflect.typeOf

//экран для просмотра обновлений статуса рейса
@Composable
fun UpdateDataScreen(
    navigateToTrackedFlights: () -> Unit, //возврат в основное приложение
    flightData: TrackedFlightUpdateData, //данные о рейсе
    differences: HashMap<String, Pair<String, String>>, //различия
    message: String
) {

    //флаг загрузки логотипа
    var loadingLogo by rememberSaveable { mutableStateOf(true) }

    //репозиторий для отслеживаемых рейсов
    val repository = koinInject<TrackedFlightsRepository>()

    //код авиалинии
    val airlineIata = flightData.flightNumber.split(' ')[0]

    //логотип авиалинии
    val logo = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(AIRLINE_LOGO_URL + airlineIata.uppercase() + ".png")
            .build(),
        onSuccess = {
            loadingLogo = false
        }
    )

    //запускаем обновление при открытии экрана
    LaunchedEffect(Unit) {
        //получаем список полей и измененных значений
        val fields = differences.map { field ->
            TrackedFlightEntity::class.memberProperties.first { it.name == field.key } to field.value.second
        }.toMap()

        //получаем имя пользователя
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(App.getInstance().applicationContext)
        val username = sharedPref.getString("activeUser", "") ?: ""

        //обновление рейса
        repository.updateFlight(
            fields = fields,
            flightNumber = flightData.flightNumber,
            date = flightData.departureTime,
            username = username
        )
    }

    //когда закончили загружать логотип, отображаем обновления
    if (!loadingLogo) {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(top = 32.dp),
            verticalArrangement = Arrangement.Center
        ) {
            if (message.isNotEmpty()) {
                Text(
                    text = message,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 36.sp
                )
                //заголовок с названием и логотипом авиалинии
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    //логотип
                    Image(
                        painter = logo,
                        contentDescription = null,
                        modifier = Modifier.size(100.dp)
                    )
                    Spacer(modifier = Modifier.width(32.dp))
                    //название
                    SubHeader(modifier = Modifier, text = flightData.flightNumber)
                }
            }
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
                    .fillMaxHeight(0.85f)
                    .padding(vertical = 32.dp, horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                item {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .padding(bottom = 24.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            //маршрут рейса
                            Text(
                                text = "${flightData.departure.first} (${flightData.departure.second})",
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            )
                            Icon(Icons.AutoMirrored.Filled.ArrowRightAlt, contentDescription = null)
                            Text(
                                text = "${flightData.arrival.first} (${flightData.arrival.second})",
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            )
                        }
                    }
                }
                //список обновлений
                items(differences.toList()) { difference ->
                    //определяем поле
                    val field = try {
                        TrackedFlightEntity::class.memberProperties.first { it.name == difference.first }
                    }
                    catch (e: NoSuchElementException) {
                        null
                    }
                    //обрабатываем старое и новое значение
                    field?.let {
                        val (oldValue, newValue) = if (field.returnType == typeOf<Long>()) {
                            val oldDate = SimpleDateFormat("MMM dd, yyyy, HH:mm", Locale.ENGLISH).format(difference.second.first.toLong())
                            val newDate = SimpleDateFormat("MMM dd, yyyy, HH:mm", Locale.ENGLISH).format(difference.second.second.toLong())
                            Pair(oldDate, newDate)
                        }
                        else {
                            difference.second
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            //выводим название поля, переведенное в обычный формат из camelCase
                            val capitalizedFieldName =
                                field.name.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])".toRegex())
                                    .joinToString(" ") {
                                        it.replaceFirstChar { char ->
                                            if (char.isLowerCase()) char.titlecase(Locale.ENGLISH) else char.toString()
                                        }
                                    }
                            SubHeader(
                                modifier = Modifier.fillMaxWidth()
                                    .padding(bottom = 8.dp),
                                capitalizedFieldName
                            )
                            //сами изменения
                            Row(
                                modifier = Modifier.padding(bottom = 16.dp)
                            ) {
                                //выводим старое значение, только если не нулевое
                                if (field.returnType == typeOf<Long>() && difference.second.first.toLong() != 0L || field.returnType == typeOf<String>() && oldValue.isNotEmpty()) {
                                    Text(
                                        text = oldValue,
                                        color = colorResource(R.color.old_data),
                                        textDecoration = TextDecoration.LineThrough,
                                        fontSize = 18.sp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Icon(Icons.AutoMirrored.Filled.ArrowRightAlt, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                }
                                //новое значение
                                Text(
                                    text = newValue,
                                    color = colorResource(R.color.updated_data),
                                    fontSize = 18.sp
                                )
                            }
                        }
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = navigateToTrackedFlights) {
                    Text("View updated flight")
                }
            }
        }
    }
}