package com.example.ft.view_flight.airline_codeshared

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        //данные об основной авиалинии
        AirlineDataComponent(
            data = mainAirline,
            logo = mainAirlineLogo,
            airlineStatus = context.getString(R.string.main_airline)
        )
        //данные о кодшеринге, если он есть
        codeshared?.let {
            codesharedLogo?.let {
                AirlineDataComponent(
                    data = codeshared,
                    logo = codesharedLogo,
                    airlineStatus = context.getString(R.string.codeshared)
                )
            }
        }
    }
}