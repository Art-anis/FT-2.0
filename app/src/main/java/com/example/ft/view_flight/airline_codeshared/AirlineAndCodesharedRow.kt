package com.example.ft.view_flight.airline_codeshared

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import com.example.ft.R
import com.example.view_flight.util.AirlineData

//данные об авиалиниях
@Composable
fun AirlineAndCodesharedRow(
    mainAirline: AirlineData,
    mainAirlineLogo: AsyncImagePainter,
    codeshared: AirlineData?,
    codesharedLogo: AsyncImagePainter?
) {
    val context = LocalContext.current
    //контейнер
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 64.dp)
            .padding(horizontal = 16.dp)
    ) {
        //заголовок секции
        Text(
            text = stringResource(R.string.airline_and_codeshared_header),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            //данные об основной авиалинии
            airlineDataComponent(
                data = mainAirline,
                logo = mainAirlineLogo,
                airlineStatus = context.getString(R.string.main_airline)
            )
            //данные о кодшеринге, если он есть
            codeshared?.let {
                codesharedLogo?.let {
                    airlineDataComponent(
                        data = codeshared,
                        logo = codesharedLogo,
                        airlineStatus = context.getString(R.string.codeshared)
                    )
                }
            }
        }
    }
}