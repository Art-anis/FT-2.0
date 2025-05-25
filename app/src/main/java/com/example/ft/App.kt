package com.example.ft

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import com.example.airport.di.airportModule
import com.example.db.di.dbModule
import com.example.ft.di.appModule
import com.example.loading.di.loadingModule
import com.example.network.di.networkModule
import com.example.search_airports.di.searchAirportsModule
import com.example.search_flights.di.searchFlightsModule
import com.example.tracked_flights.di.trackedFlightsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

//файл приложения
class App: Application() {

    companion object {
        private lateinit var instance: App

        fun getInstance(): App {
            return instance
        }
    }

    //при запуске приложения
    override fun onCreate() {
        super.onCreate()
        instance = this

        //запускаем di
        startKoin {
            androidContext(applicationContext)

            androidLogger(Level.DEBUG)

            modules(listOf(networkModule, dbModule, loadingModule, searchAirportsModule,
                searchFlightsModule, airportModule,
                trackedFlightsModule, appModule))
        }

        createNotificationChannel()
    }

    //создание канала уведомления
    private fun createNotificationChannel() {
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel("0", "Flight reminder", importance)
        channel.description = "Reminds you about your flights and their status updates."
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }
}