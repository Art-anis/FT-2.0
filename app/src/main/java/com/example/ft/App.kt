package com.example.ft

import android.app.Application
import com.example.db.di.dbModule
import com.example.loading.di.loadingModule
import com.example.network.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

//файл приложения
class App: Application() {

    //при запуске приложения
    override fun onCreate() {
        super.onCreate()

        //запускаем di
        startKoin {
            androidContext(applicationContext)

            androidLogger(Level.DEBUG)

            modules(listOf(networkModule, dbModule, loadingModule))
        }
    }
}