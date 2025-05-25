package com.example.ft

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.ft.ui.theme.FTTheme
import com.example.ft.update_tracked_data.UpdateDataScreen
import com.example.ft.util.TrackedFlightUpdateData

//просмотр данных об обновлении рейса
class UpdateDataActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        //собираем данные из intent
        val flightNumber = intent.getStringExtra("flightNumber")
        val differences = intent.getSerializableExtra("differences") as? HashMap<String, Pair<String, String>> ?: HashMap()
        val departure = intent.getSerializableExtra("departure") as? Pair<String, String>
        val arrival = intent.getSerializableExtra("arrival") as? Pair<String, String>
        val departureTime = intent.getLongExtra("departureTime", 0)
        setContent {
            FTTheme {
                //сам экран
                UpdateDataScreen(
                    differences = differences,
                    navigateToTrackedFlights = {
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    },
                    flightData = TrackedFlightUpdateData(
                        flightNumber = flightNumber ?: "",
                        departure = departure ?: Pair("", ""),
                        arrival = arrival ?: Pair("", ""),
                        departureTime = departureTime
                    )
                )
            }
        }
    }
}